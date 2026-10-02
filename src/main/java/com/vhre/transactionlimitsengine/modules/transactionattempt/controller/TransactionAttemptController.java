package com.vhre.transactionlimitsengine.modules.transactionattempt.controller;

import com.vhre.base.core.base.controller.BaseController;
import com.vhre.transactionlimitsengine.modules.transactionattempt.dto.TransactionAttemptDTO;
import com.vhre.transactionlimitsengine.modules.transactionattempt.entity.TransactionAttempt;
import com.vhre.transactionlimitsengine.modules.transactionattempt.service.TransactionAttemptService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transaction-attempts")
@Tag(name = "Transaction Attempt Management", description = "Endpoints for managing Transaction Attempt")
public class TransactionAttemptController extends BaseController<TransactionAttempt, TransactionAttemptDTO, UUID> {

    public TransactionAttemptController(TransactionAttemptService service) {
        super(service);
    }
}
