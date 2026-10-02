package com.vhre.transactionlimitsengine.modules.userlimitassignment.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vhre.base.core.base.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Schema(description = "Data Transfer Object representing a User Limit Assignment (user to limit profile)")
@JsonPropertyOrder({"id", "userId", "limitProfileId", "createdAt", "updatedAt", "deleted"})
public class UserLimitAssignmentDTO extends BaseDTO {

    @Schema(description = "UUID del usuario al que se asigna el perfil (único: un usuario tiene un único perfil vigente).", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private UUID userId;

    @Schema(description = "UUID del perfil de límites asignado al usuario. Debe existir.", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private UUID limitProfileId;
}
