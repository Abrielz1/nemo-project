package ru.nemo_project.nemo_project.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.nemo_project.nemo_project.domen.model.BalanceDTO;
import ru.nemo_project.nemo_project.domen.model.CreateBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.DeleteBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.GetBalanceRequestDTO;
import ru.nemo_project.nemo_project.domen.model.UpdateBalanceRequestDTO;
import ru.nemo_project.nemo_project.repository.OverTimeRepositoryDAO;
import ru.nemo_project.nemo_project.service.facade.OvertimeFacade;

@Slf4j
@Component
@RequiredArgsConstructor
public class OvertimeFacadeImpl implements OvertimeFacade {

    private final OverTimeRepositoryDAO overTimeRepositoryDAO;

    @Override
    public BalanceDTO getBalance(GetBalanceRequestDTO request) {

        return this.overTimeRepositoryDAO.getBalance(request);
    }

    @Override
    public BalanceDTO createBalance(CreateBalanceRequestDTO request) {

       return this.overTimeRepositoryDAO.createBalance(request);
    }

    @Override
    public BalanceDTO updateBalance(UpdateBalanceRequestDTO request) {

       return this.overTimeRepositoryDAO.updateBalance(request);
    }

    @Override
    public boolean deleteBalance(DeleteBalanceRequestDTO request) {

        return this.overTimeRepositoryDAO.deleteBalance(request);
    }
}
