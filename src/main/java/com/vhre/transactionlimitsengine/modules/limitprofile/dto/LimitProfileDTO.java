package com.vhre.transactionlimitsengine.modules.limitprofile.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vhre.base.core.base.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Data Transfer Object representing a Limit Profile (transaction limits by tier)")
@JsonPropertyOrder({"id", "tierName", "dailyMax", "monthlyMax", "perOpMax", "createdAt", "updatedAt", "deleted"})
public class LimitProfileDTO extends BaseDTO {

    @Schema(description = "Nombre del nivel (tier) del perfil de límites (único por perfil).", example = "GOLD")
    @NotBlank
    @Size(max = 50)
    private String tierName;

    @Schema(description = "Límite máximo acumulado por día (0 = tier bloqueado).", example = "20000.0000")
    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 14, fraction = 4)
    private BigDecimal dailyMax;

    @Schema(description = "Límite máximo acumulado por mes (0 = tier bloqueado).", example = "200000.0000")
    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 14, fraction = 4)
    private BigDecimal monthlyMax;

    @Schema(description = "Límite máximo por operación individual (0 = tier bloqueado).", example = "5000.0000")
    @NotNull
    @DecimalMin(value = "0.0")
    @Digits(integer = 14, fraction = 4)
    private BigDecimal perOpMax;
}
