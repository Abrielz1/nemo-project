package ru.nemo_project.nemo_project.domen.model;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateBalanceRequestDTO(UUID employeeId, BigDecimal initialMinutes) {
}
