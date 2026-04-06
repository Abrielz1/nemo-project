package ru.nemo_project.controller;

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

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class OvertimeBalanceServiceImpl extends OvertimeBalanceServiceGrpc.OvertimeBalanceServiceImplBase {

    @Override
    public void getBalance(GetBalanceRequest request, StreamObserver<OvertimeBalance> responseObserver) {
        super.getBalance(request, responseObserver);
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
