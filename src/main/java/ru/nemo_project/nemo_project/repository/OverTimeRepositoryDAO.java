package ru.nemo_project.nemo_project.repository;

import ru.nemo_project.nemo_project.domen.model.BalanceDTO;
import ru.nemo_project.nemo_project.domen.model.CreateBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.DeleteBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.GetBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.UpdateBalanceRequestDTO;

public interface OverTimeRepositoryDAO {

    BalanceDTO getBalance(GetBalanceRequestDTO request);

    BalanceDTO createBalance(CreateBalanceRequestDTO request);

    BalanceDTO updateBalance(UpdateBalanceRequestDTO request);

    boolean deleteBalance(DeleteBalanceRequestDTO request);
}
