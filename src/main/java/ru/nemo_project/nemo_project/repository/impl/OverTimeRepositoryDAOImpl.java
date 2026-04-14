package ru.nemo_project.nemo_project.repository.impl;

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
import java.util.concurrent.atomic.AtomicReference;

@Repository
@RequiredArgsConstructor
public class OverTimeRepositoryDAOImpl implements OverTimeRepositoryDAO {

    private final Mapper mapper;

    private final Map<String, EmployeeData> repository = new ConcurrentHashMap<>();

    private record State(long id, long version) {}

    private final AtomicReference<State> counter = new AtomicReference<>(new State(1L, 0L));

    @Override
    public BalanceDTO getBalance(GetBalanceRequest request) {

        return this.mapper.fromEmployeeDats(this.repository.get(request.getEmployeeId()));
    }

    @Override
    public BalanceDTO createBalance(CreateBalanceRequest request) {

       State nextState = counter.updateAndGet(s -> new State(s.id() + 1, s.version() + 1));
        long id= nextState.id();

        var newBalance = this.mapper.fromCreateBalanceRequestDto(request);

        newBalance.setId(id);
        newBalance.setVersion(0L);
        newBalance.setUpdatedAt(Instant.now());
        this.repository.put(newBalance.getEmployeeId().toString(), newBalance);
      return this.mapper.fromEmployeeDats(newBalance);
    }

    @Override
    public BalanceDTO updateBalance(UpdateBalanceRequest request) {

        var currentBalance = this.repository.get(request.getEmployeeId());
       currentBalance.setOvertimeMinutesAccumulated(new BigDecimal(request.getMinutesToChange()));
        this.repository.put(request.getEmployeeId(), currentBalance);
        return this.mapper.fromEmployeeDats(currentBalance);
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
