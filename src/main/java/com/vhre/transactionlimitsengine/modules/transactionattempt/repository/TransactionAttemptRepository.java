package com.vhre.transactionlimitsengine.modules.transactionattempt.repository;

import com.vhre.transactionlimitsengine.modules.transactionattempt.entity.TransactionAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionAttemptRepository extends JpaRepository<TransactionAttempt, UUID> {
}
