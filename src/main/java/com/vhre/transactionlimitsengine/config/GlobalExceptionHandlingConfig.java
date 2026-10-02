package com.vhre.transactionlimitsengine.config;

import com.vhre.base.core.exceptions.GlobalExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * The starter's advice lives in {@code com.vhre.base}, outside this
 * application's component scan, so it must be imported to become active.
 */
@Configuration
@Import(GlobalExceptionHandler.class)
public class GlobalExceptionHandlingConfig {
}
