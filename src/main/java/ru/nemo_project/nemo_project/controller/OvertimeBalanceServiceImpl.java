package ru.nemo_project.nemo_project.controller;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.DeleteBalanceRequest;
import ru.nemo_project.hr.grpc.GetBalanceRequest;
import ru.nemo_project.hr.grpc.OvertimeBalance;
import ru.nemo_project.hr.grpc.OvertimeBalanceServiceGrpc;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import ru.nemo_project.nemo_project.service.facade.OvertimeFacade;
import ru.nemo_project.nemo_project.util.Validator;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class OvertimeBalanceServiceImpl extends OvertimeBalanceServiceGrpc.OvertimeBalanceServiceImplBase {

    private final OvertimeFacade overtimeFacade;

    private final Validator validator;

    @Override
    public void getBalance(GetBalanceRequest request, StreamObserver<OvertimeBalance> responseObserver) {

        if (this.validator.uuidValidator(request.getEmployeeId())) {
            throw new IllegalArgumentException("");
        }

       var response = this.overtimeFacade.getBalance(request);

        if (this.validator.balanceAmountValidator(response.overtimeMinutesAccumulated())) {
            throw new IllegalArgumentException("");
        }
    }

    @Override
    public void createBalance(CreateBalanceRequest request, StreamObserver<OvertimeBalance> responseObserver) {
        super.createBalance(request, responseObserver);
    }

    @Override
    public void updateBalance(UpdateBalanceRequest request, StreamObserver<OvertimeBalance> responseObserver) {
        super.updateBalance(request, responseObserver);
    }

    @Override
    public void deleteBalance(DeleteBalanceRequest request, StreamObserver<Empty> responseObserver) {
        super.deleteBalance(request, responseObserver);
    }
}
