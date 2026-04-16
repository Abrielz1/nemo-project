package ru.nemo_project.nemo_project.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.DeleteBalanceRequest;
import ru.nemo_project.hr.grpc.GetBalanceRequest;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import ru.nemo_project.nemo_project.domen.model.BalanceDTO;
import ru.nemo_project.nemo_project.repository.OverTimeRepositoryDAO;
import ru.nemo_project.nemo_project.service.facade.OvertimeFacade;
import ru.nemo_project.nemo_project.util.Mapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class OvertimeFacadeImpl implements OvertimeFacade {

    private final Mapper mapper;

    private final OverTimeRepositoryDAO overTimeRepositoryDAO;

    @Override
    public BalanceDTO getBalance(GetBalanceRequest request) {

        return this.overTimeRepositoryDAO.getBalance(this.mapper.getBalanceRequest(request));
    }

    @Override
    public BalanceDTO createBalance(CreateBalanceRequest request) {

       return this.overTimeRepositoryDAO.createBalance(this.mapper.toCreateBalanceRequestDto(request));
    }

    @Override
    public BalanceDTO updateBalance(UpdateBalanceRequest request) {

       return this.overTimeRepositoryDAO.updateBalance(this.mapper.toUpdateBalanceRequestDto(request));
    }

    @Override
    public boolean deleteBalance(DeleteBalanceRequest request) {

        return this.overTimeRepositoryDAO.deleteBalance(this.mapper.toDeleteBalanceRequesDto(request));
    }
}
