package com.vhre.transactionlimitsengine.modules.transactionattempt.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vhre.base.core.base.dto.BaseDTO;
import com.vhre.transactionlimitsengine.modules.transactionattempt.enums.TransactionStatus;
import com.vhre.transactionlimitsengine.modules.transactionattempt.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Schema(description = "Data Transfer Object representing a Transaction Attempt")
@JsonPropertyOrder({"id", "userId", "amount", "type", "status", "reason", "createdAt", "updatedAt", "deleted"})
public class TransactionAttemptDTO extends BaseDTO {

    @Schema(description = "UUID del usuario que realizó la operación (debe tener una asignación de perfil vigente).", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private UUID userId;

    @Schema(description = "Monto de la operación intentada.", example = "150.0000")
    @NotNull
    @Positive
    @Digits(integer = 14, fraction = 4)
    private BigDecimal amount;

    @Schema(description = "Tipo de operación intentada.", example = "DEPOSIT")
    @NotNull
    private TransactionType type;

    @Schema(description = "Resultado de la evaluación de límites del intento.", example = "APPROVED")
    @NotNull
    private TransactionStatus status;

    @Schema(description = "Razón del rechazo (solo cuando el estado es REJECTED_*).", example = "Daily limit exceeded")
    @Size(max = 255)
    private String reason;
}
