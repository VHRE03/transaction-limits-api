package com.vhre.transactionlimitsengine.modules.transactionattempt.service;

import com.vhre.base.core.base.service.BaseServiceImpl;
import com.vhre.transactionlimitsengine.modules.transactionattempt.dto.TransactionAttemptDTO;
import com.vhre.transactionlimitsengine.modules.transactionattempt.entity.TransactionAttempt;
import com.vhre.transactionlimitsengine.modules.transactionattempt.mapper.TransactionAttemptMapper;
import com.vhre.transactionlimitsengine.modules.transactionattempt.repository.TransactionAttemptRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionAttemptServiceImpl extends BaseServiceImpl<TransactionAttempt, TransactionAttemptDTO, UUID>
        implements TransactionAttemptService {

    public TransactionAttemptServiceImpl(TransactionAttemptRepository repository, TransactionAttemptMapper mapper) {
        super(repository, mapper);
    }
}
