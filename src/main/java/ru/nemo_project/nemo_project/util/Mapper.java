package ru.nemo_project.nemo_project.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.nemo_project.nemo_project.domen.entity.EmployeeData;
import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.OvertimeBalance;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import ru.nemo_project.nemo_project.domen.model.BalanceDTO;
import ru.nemo_project.nemo_project.domen.model.CreateBalanceRequestDTO;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final ParserUUID parserUUID;

    public OvertimeBalance toOvertimeBalanceDto(BalanceDTO employeeData) {

       if (employeeData == null) {
           return OvertimeBalance.newBuilder().build();
       }

       if (employeeData.employeeId() == null) {
           return  OvertimeBalance.newBuilder().build();
       }

       if (employeeData.overtimeMinutesAccumulated() == null) {
           return OvertimeBalance.newBuilder().build();
       }

        return OvertimeBalance.newBuilder()
                .setEmployeeId(employeeData.employeeId())
                .setMinutesAccumulated(employeeData.overtimeMinutesAccumulated().toString())
                .build();
    }

    public BalanceDTO toOvertimeBalanceDto(EmployeeData employeeData) {

        if (employeeData == null) {
            throw new IllegalArgumentException("Data from DB id null");
        }

        if (employeeData.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee id must not be null");
        }

        if (employeeData.getOvertimeMinutesAccumulated() == null) {
            throw new IllegalArgumentException("Employee OvertimeMinutes Accumulated must not be null");
        }

        return new BalanceDTO(String.valueOf(employeeData.getEmployeeId()), employeeData.getOvertimeMinutesAccumulated());
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

    public EmployeeData fromCreateBalanceRequestDto(CreateBalanceRequestDTO request) {

       return EmployeeData.builder()
                .employeeId(this.parserUUID.parseUUIDFromGRPC(request.employeeId()))
                .overtimeMinutesAccumulated(new BigDecimal(request.initialMinutes()))
                .build();
    }

    public BalanceDTO fromEmployeeDats(EmployeeData employeeData) {

        return new BalanceDTO(employeeData.getEmployeeId().toString(), employeeData.getOvertimeMinutesAccumulated());
    }
}
