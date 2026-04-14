package ru.nemo_project.nemo_project.repository;

import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.DeleteBalanceRequest;
import ru.nemo_project.hr.grpc.GetBalanceRequest;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import ru.nemo_project.nemo_project.domen.model.BalanceDTO;

public interface OverTimeRepositoryDAO {

    BalanceDTO getBalance(GetBalanceRequest request);

    BalanceDTO createBalance(CreateBalanceRequest request);

    BalanceDTO updateBalance(UpdateBalanceRequest request);

    boolean deleteBalance(DeleteBalanceRequest request);
}
