package ru.nemo_project.nemo_project.repository.impl;

import io.grpc.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.DeleteBalanceRequest;
import ru.nemo_project.hr.grpc.GetBalanceRequest;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import ru.nemo_project.nemo_project.domen.entity.EmployeeData;
import ru.nemo_project.nemo_project.domen.model.BalanceDTO;
import ru.nemo_project.nemo_project.repository.OverTimeRepositoryDAO;
import ru.nemo_project.nemo_project.util.Mapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

@Repository
@RequiredArgsConstructor
public class OverTimeRepositoryDAOImpl implements OverTimeRepositoryDAO {

    private final Mapper mapper;

    private final Map<String, EmployeeData> repository = new ConcurrentHashMap<>();

    private final Map<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    private record State(long id, long version) {
    }

    private final AtomicReference<State> counter = new AtomicReference<>(new State(1L, 0L));

    @Override
    public BalanceDTO getBalance(GetBalanceRequest request) {

        return this.mapper.fromEmployeeDats(this.repository.get(request.getEmployeeId()));
    }

    @Override
    public BalanceDTO createBalance(CreateBalanceRequest request) {

        State nextState = counter.updateAndGet(s -> new State(s.id() + 1, s.version() + 1));
        long id = nextState.id();

        var newBalance = this.mapper.fromCreateBalanceRequestDto(request);

        newBalance.setId(id);
        newBalance.setVersion(0L);
        newBalance.setUpdatedAt(Instant.now());
        this.repository.put(newBalance.getEmployeeId().toString(), newBalance);
        return this.mapper.fromEmployeeDats(newBalance);
    }

    @Override
    public BalanceDTO updateBalance(UpdateBalanceRequest request) {

        String empId = request.getEmployeeId();
        int attempts = 0;
        ReentrantLock userLock = locks.computeIfAbsent(empId, k -> new ReentrantLock());

        while (attempts < 3) {

            try {

                if (userLock.tryLock(50, TimeUnit.MILLISECONDS)) {

                    try {

                        var currentBalance = this.repository.get(request.getEmployeeId());

                        if (currentBalance == null) {

                            throw new IllegalArgumentException("");
                        }

                        currentBalance.setOvertimeMinutesAccumulated(new BigDecimal(request.getMinutesToChange()));
                        this.repository.put(request.getEmployeeId(), currentBalance);
                        return this.mapper.fromEmployeeDats(currentBalance);

                    } finally {
                        userLock.unlock();
                    }

                } else {
                    attempts++;
                    Thread.sleep(100 * attempts);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted", e);
            }
        }

        throw Status.ABORTED.withDescription("Could not acquire lock for update, system is busy").asRuntimeException();
    }

    @Override
    public boolean deleteBalance(DeleteBalanceRequest request) {

        if (this.repository.containsKey(request.getEmployeeId())) {
            this.repository.remove(request.getEmployeeId());
            return true;
        }

        return false;
    }
}
