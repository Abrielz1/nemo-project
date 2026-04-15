package ru.nemo_project.nemo_project.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.nemo_project.hr.grpc.CreateBalanceRequest;
import ru.nemo_project.hr.grpc.DeleteBalanceRequest;
import ru.nemo_project.hr.grpc.GetBalanceRequest;
import ru.nemo_project.hr.grpc.UpdateBalanceRequest;
import ru.nemo_project.nemo_project.domen.model.BalanceDTO;
import ru.nemo_project.nemo_project.service.facade.OvertimeFacade;
import ru.nemo_project.nemo_project.util.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class OvertimeFacadeImpl implements OvertimeFacade {

    private final Validator validator;

    private final OvertimeFacade overtimeFacade;


    @Override
    public BalanceDTO getBalance(GetBalanceRequest request) {

        if (!this.validator.uuidValidator(request.getEmployeeId())) {
            throw new IllegalArgumentException("");
        }

        var fromDB = this.overtimeFacade.getBalance(request);

        return new BalanceDTO(null, null);
    }

    @Override
    public void createBalance(CreateBalanceRequest request) {

    }

    @Override
    public void updateBalance(UpdateBalanceRequest request) {

    }

    @Override
    public void deleteBalance(DeleteBalanceRequest request) {

    }
}
