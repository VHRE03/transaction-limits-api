package com.vhre.transactionlimitsengine.modules.transactionattempt.enums;

public enum TransactionStatus {
    APPROVED,
    REJECTED_DAILY_LIMIT,
    REJECTED_MONTHLY_LIMIT,
    REJECTED_OPERATION_LIMIT,
    SYSTEM_ERROR
}
