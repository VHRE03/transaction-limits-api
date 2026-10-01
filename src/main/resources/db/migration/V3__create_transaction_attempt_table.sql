CREATE TABLE transaction_attempts
(
    id         UUID           NOT NULL,
    user_id    UUID           NOT NULL,
    amount     NUMERIC(18, 4) NOT NULL,
    type       VARCHAR(20)    NOT NULL,
    status     VARCHAR(30)    NOT NULL,
    reason     VARCHAR(255),
    created_at TIMESTAMP      NOT NULL,
    updated_at TIMESTAMP NULL,
    is_deleted BOOLEAN        NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_transaction_attempts PRIMARY KEY (id),
    CONSTRAINT ck_transaction_attempts_type CHECK (type IN ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_OUT', 'PAYMENT')),
    CONSTRAINT ck_transaction_attempts_status CHECK (status IN ('APPROVED', 'REJECTED_DAILY_LIMIT', 'REJECTED_MONTHLY_LIMIT', 'REJECTED_OPERATION_LIMIT', 'SYSTEM_ERROR')),
    CONSTRAINT fk_transaction_attempts_user_limit_assignments FOREIGN KEY (user_id) REFERENCES user_limit_assignments (user_id)
);

CREATE INDEX ix_transaction_attempts_user_id_created_at ON transaction_attempts (user_id, created_at);
