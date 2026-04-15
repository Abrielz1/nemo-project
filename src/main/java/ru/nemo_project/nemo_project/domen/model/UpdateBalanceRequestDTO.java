package ru.nemo_project.nemo_project.domen.model;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateBalanceRequestDTO(UUID employeeId, BigDecimal minutesToChange) {
}
