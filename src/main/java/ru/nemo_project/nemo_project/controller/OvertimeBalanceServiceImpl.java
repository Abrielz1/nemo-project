package ru.nemo_project.nemo_project.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.util.StringUtils;
import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.DeleteBalanceRequest;
import ru.nemo_project.hr.grpc.GetBalanceRequest;
import ru.nemo_project.hr.grpc.OvertimeBalance;
import ru.nemo_project.hr.grpc.OvertimeBalanceServiceGrpc;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import ru.nemo_project.nemo_project.service.facade.OvertimeFacade;
import ru.nemo_project.nemo_project.util.Mapper;
import ru.nemo_project.nemo_project.util.Validator;
import java.math.BigDecimal;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class OvertimeBalanceServiceImpl extends OvertimeBalanceServiceGrpc.OvertimeBalanceServiceImplBase {

    private final OvertimeFacade overtimeFacade;

    private final Validator validator;

    private final Mapper mapper;

    @Override
    public void getBalance(GetBalanceRequest request, StreamObserver<OvertimeBalance> responseObserver) {

        if (!this.validator.uuidValidator(request.getEmployeeId())) {
            this.sendError(responseObserver, Status.INVALID_ARGUMENT, "Invalid UUID format");
            return;
        }

       var response = this.overtimeFacade.getBalance(this.mapper.getBalanceRequest(request));

        if (response == null) {
            sendError(responseObserver, Status.NOT_FOUND, "Balance not found for this employee");
            return;
        }

        if (!this.validator.balanceAmountValidator(response.overtimeMinutesAccumulated())) {
            sendError(responseObserver, Status.INVALID_ARGUMENT, "Balance are invalid for this employee");
            return;
        }

        responseObserver.onNext(this.mapper.toOvertimeBalanceDto(response));
        responseObserver.onCompleted();
    }

    @Override
    public void createBalance(CreateBalanceRequest request, StreamObserver<OvertimeBalance> responseObserver) {

        if (!this.validator.uuidValidator(request.getEmployeeId())) {
            this.sendError(responseObserver, Status.INVALID_ARGUMENT, "Invalid UUID format");
            return;
        }

        var newBalance = new BigDecimal(request.getInitialMinutes());
        if (!this.validator.balanceAmountValidator(newBalance)) {
            sendError(responseObserver, Status.INVALID_ARGUMENT, "Balance are invalid for this employee");
            return;
        }

        var responseDto = this.mapper.toCreateBalanceRequestDto(request, newBalance);

        var response = this.overtimeFacade.createBalance(responseDto);

        if (!this.validator.balanceAmountValidator(response.overtimeMinutesAccumulated())) {
            sendError(responseObserver, Status.INVALID_ARGUMENT, "Balance are invalid for this employee");
            return;
        }

        var grpcResponse = this.mapper.toOvertimeBalanceDto(response)фдд вщту;

        responseObserver.onNext(grpcResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBalance(UpdateBalanceRequest request, StreamObserver<OvertimeBalance> responseObserver) {

        if (!this.validator.uuidValidator(request.getEmployeeId())) {
            this.sendError(responseObserver, Status.INVALID_ARGUMENT, "Invalid UUID format");
            return;
        }

        if (!StringUtils.hasText(request.getMinutesToChange())) {
            sendError(responseObserver, Status.INVALID_ARGUMENT, "Balance are invalid for this employee");
            return;
        }

        var newBalance = new BigDecimal(request.getMinutesToChange());
        if (!this.validator.balanceAmountValidator(newBalance)) {
            sendError(responseObserver, Status.INVALID_ARGUMENT, "Balance are invalid for this employee");
            return;
        }

        var responseDto = this.mapper.toUpdateBalanceRequestDto(request);

        var fromBd = this.overtimeFacade.updateBalance(responseDto);

        if (fromBd == null) {
            sendError(responseObserver, Status.NOT_FOUND, "Employee balance not found for update");
            return;
        }

        if (!this.validator.balanceAmountValidator(fromBd.overtimeMinutesAccumulated())) {
            sendError(responseObserver, Status.INVALID_ARGUMENT, "Balance are invalid for this employee");
            return;
        }

        var response = this.mapper.toOvertimeBalanceDto(fromBd);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBalance(DeleteBalanceRequest request, StreamObserver<Empty> responseObserver) {
        if (!this.validator.uuidValidator(request.getEmployeeId())) {
            this.sendError(responseObserver, Status.INVALID_ARGUMENT, "Invalid UUID format");
            return;
        }
        var response = this.overtimeFacade.deleteBalance(this.mapper.toDeleteBalanceRequesDto(request));

        if (!response) {
            sendError(responseObserver, Status.NOT_FOUND, "Balance not found for this employee");
            return;
        }

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    private void sendError(StreamObserver<?> observer, io.grpc.Status status, String description) {
        observer.onError(status.withDescription(description).asRuntimeException());
    }
}
