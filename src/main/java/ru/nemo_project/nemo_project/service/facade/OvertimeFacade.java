package ru.nemo_project.nemo_project.service.facade;

import ru.nemo_project.nemo_project.domen.model.BalanceDTO;
import ru.nemo_project.nemo_project.domen.model.CreateBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.DeleteBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.GetBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.UpdateBalanceRequestDTO;

public interface OvertimeFacade {

    BalanceDTO getBalance(GetBalanceRequestDTO request);

    BalanceDTO createBalance(CreateBalanceRequestDTO request);

    BalanceDTO updateBalance(UpdateBalanceRequestDTO request);

    boolean deleteBalance(DeleteBalanceRequestDTO request);
}
