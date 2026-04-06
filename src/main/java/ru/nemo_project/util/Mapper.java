package ru.nemo_project.util;

import lombok.experimental.UtilityClass;
import ru.nemo_project.domen.entity.EmployeeData;
import ru.nemo_project.hr.grpc.OvertimeBalance;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;

import java.math.BigDecimal;

@UtilityClass
public class Mapper {

    public OvertimeBalance toOvertimeBalanceDto(EmployeeData employeeData) {

       if (employeeData == null) return OvertimeBalance.newBuilder().build();
       if (employeeData.getOvertimeMinutesAccumulated() == null) return OvertimeBalance.newBuilder().build();

        return OvertimeBalance.newBuilder()
                .setEmployeeId(employeeData.getEmployeeId().toString())
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
}
