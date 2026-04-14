package ru.nemo_project.nemo_project.domen.model;

import java.math.BigDecimal;

public record BalanceDTO(String employeeId, BigDecimal overtimeMinutesAccumulated) {
}
