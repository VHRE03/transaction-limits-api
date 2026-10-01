package com.vhre.transactionlimitsengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Entry point of the Transaction Limits API (monolithic application).
 *
 * <p>{@code @EnableJpaAuditing} is required by the internal base starter:
 * {@code BaseEntity} relies on Spring Data auditing to populate the
 * {@code createdAt} and {@code updatedAt} columns automatically.</p>
 */
@SpringBootApplication
@EnableJpaAuditing
public class TransactionLimitsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionLimitsApiApplication.class, args);
    }

}
