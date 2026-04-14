package ru.nemo_project.nemo_project.util;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;

@Slf4j
@Component
public class Validator {

    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public boolean uuidValidator(String employeeId) {

        return StringUtils.hasText(employeeId) && UUID_REGEX.matcher(employeeId).matches();
    }

    public boolean balanceAmountValidator(BigDecimal bigDecimal) {

        if (bigDecimal != null && bigDecimal.compareTo(BigDecimal.ZERO) > 0) {
            return false;
        }

        return true;
    }
}
