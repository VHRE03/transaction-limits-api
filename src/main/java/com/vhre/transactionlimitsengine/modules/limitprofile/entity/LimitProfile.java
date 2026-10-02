package com.vhre.transactionlimitsengine.modules.limitprofile.entity;

import com.vhre.base.core.base.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "limit_profiles", uniqueConstraints = {
        @UniqueConstraint(name = "uq_limit_profiles_tier_name", columnNames = "tier_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LimitProfile extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String tierName;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal dailyMax;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal monthlyMax;

    @Column(nullable = false, precision = 18, scale = 4)
    private BigDecimal perOpMax;
}
