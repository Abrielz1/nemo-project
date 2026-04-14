package ru.nemo_project.nemo_project.service.facade;

import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.DeleteBalanceRequest;
import ru.nemo_project.hr.grpc.GetBalanceRequest;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import ru.nemo_project.nemo_project.domen.model.BalanceDTO;

public interface OvertimeFacade {

    BalanceDTO getBalance(GetBalanceRequest request);

    void createBalance(CreateBalanceRequest request);

    void updateBalance(UpdateBalanceRequest request);

    void deleteBalance(DeleteBalanceRequest request);
}
