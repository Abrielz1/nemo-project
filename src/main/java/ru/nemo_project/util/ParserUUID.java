package ru.nemo_project.util;

import io.grpc.Status;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.UUID;

@Component
public class ParserUUID {

    public UUID parseUUIDFromGRPC(String inputUUID) {

        if (!StringUtils.hasText(inputUUID)) {
            throw Status
                    .INVALID_ARGUMENT
                    .withDescription("UUID must not be empty")
                    .asRuntimeException();
        }

        try {
           return UUID.fromString(inputUUID);
        } catch (IllegalArgumentException e) {
           throw io.grpc.Status.INVALID_ARGUMENT
                    .withDescription("Invalid UUID format for Employee ID: " + inputUUID)
                    .asRuntimeException();
        }
    }
}
