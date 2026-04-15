package ru.nemo_project.nemo_project.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.nemo_project.nemo_project.domen.entity.EmployeeData;
import ru.nemo_project.nemo_project.domen.model.BalanceDTO;
import ru.nemo_project.nemo_project.domen.model.CreateBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.DeleteBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.GetBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.UpdateBalanceRequestDTO;
import ru.nemo_project.nemo_project.repository.OverTimeRepositoryDAO;
import ru.nemo_project.nemo_project.util.Mapper;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

@Repository
@RequiredArgsConstructor
public class OverTimeRepositoryDAOImpl implements OverTimeRepositoryDAO {

    private final Mapper mapper;

    private final Map<UUID, EmployeeData> repository = new ConcurrentHashMap<>();

    private final Map<UUID, ReentrantLock> locks = new ConcurrentHashMap<>();

    private record State(long id, long version) {
    }

    private final AtomicReference<State> counter = new AtomicReference<>(new State(1L, 0L));

    @Override
    public BalanceDTO getBalance(GetBalanceRequestDTO request) {
       if (request.employeeId() == null) {
           throw new IllegalArgumentException("");
       }

        return this.mapper.fromEmployeeDats(this.repository.getOrDefault(request.employeeId(), EmployeeData.builder().build()));
    }

    @Override
    public BalanceDTO createBalance(CreateBalanceRequestDTO request) {

        State nextState = counter.updateAndGet(s -> new State(s.id() + 1, s.version() + 1));
        long id = nextState.id();

        var newBalance = this.mapper.fromCreateBalanceRequestDto(request);

        newBalance.setId(id);
        newBalance.setVersion(0L);
        newBalance.setUpdatedAt(Instant.now());

        if (this.repository.putIfAbsent(request.employeeId(), newBalance) != null) {
            throw new IllegalStateException("Balance already exists!");
        }

        return this.mapper.fromEmployeeDats(newBalance);
    }

    @Override
    public BalanceDTO updateBalance(UpdateBalanceRequestDTO request) {

        var empId = request.employeeId();
        var minutesDelta = request.minutesToChange();
        int attempts = 0;
        ReentrantLock userLock = locks.computeIfAbsent(empId, k -> new ReentrantLock());

        while (attempts < 3) {

            try {

                if (userLock.tryLock(50, TimeUnit.MILLISECONDS)) {

                    try {

                        var currentBalance = this.repository.get(empId);

                        if (currentBalance == null) {

                            throw new IllegalArgumentException("");
                        }

                        var currentMinutes = currentBalance.getOvertimeMinutesAccumulated();
                        currentBalance.setOvertimeMinutesAccumulated(currentMinutes.add(minutesDelta));
                        this.repository.put(empId, currentBalance);
                        return this.mapper.fromEmployeeDats(currentBalance);

                    } finally {
                        userLock.unlock();
                    }

                } else {
                    attempts++;
                    Thread.sleep(100L * attempts);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread was interrupted", e);
            }
        }

        throw new RuntimeException("Could not acquire lock for update, system is busy");
    }

    @Override
    public boolean deleteBalance(DeleteBalanceRequestDTO request) {

        return this.repository.remove(request.employeeId()) != null;
    }
}
