package ru.nemo_project.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nemo_project.domen.entity.EmployeeData;
import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.OvertimeBalance;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import java.math.BigDecimal;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final ParserUUID parserUUID;

    public OvertimeBalance toOvertimeBalanceDto(EmployeeData employeeData) {

       if (employeeData == null) return OvertimeBalance.newBuilder().build();
       if (employeeData.getEmployeeId() == null) return  OvertimeBalance.newBuilder().build();
       if (employeeData.getOvertimeMinutesAccumulated() == null) return OvertimeBalance.newBuilder().build();

        return OvertimeBalance.newBuilder()
                .setEmployeeId(String.valueOf(employeeData.getEmployeeId()))
                .setMinutesAccumulated(employeeData.getOvertimeMinutesAccumulated().longValue())
                .build();
    }

    public EmployeeData fromUpdateBalanceRequestDto(EmployeeData employeeData, UpdateBalanceRequest updateBalanceRequest) {

        if (updateBalanceRequest.getMinutesToChange()== 0l) {
            return employeeData;
        }

        if (employeeData.getOvertimeMinutesAccumulated() == null ) {
            employeeData.setOvertimeMinutesAccumulated(BigDecimal.ZERO);
        }

      var currentWorkingMinutes = employeeData.getOvertimeMinutesAccumulated();
      var newMinutes = new BigDecimal(updateBalanceRequest.getMinutesToChange());
      var newWorkingMinutes = currentWorkingMinutes.add(newMinutes);

      employeeData.setOvertimeMinutesAccumulated(newWorkingMinutes);
        return employeeData;
    }

    public EmployeeData fromCreateBalanceRequestDto(CreateBalanceRequest request) {

       return EmployeeData.builder()
                .employeeId(this.parserUUID.parseUUIDFromGRPC(request.getEmployeeId()))
                .overtimeMinutesAccumulated(new BigDecimal(request.getInitialMinutes()))
                .build();
    }
}
