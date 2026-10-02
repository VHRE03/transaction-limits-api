package com.vhre.transactionlimitsengine.modules.userlimitassignment.entity;

import com.vhre.base.core.base.entity.BaseEntity;
import com.vhre.transactionlimitsengine.modules.limitprofile.entity.LimitProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_limit_assignments", uniqueConstraints = {
        @UniqueConstraint(name = "uq_user_limit_assignments_user_id", columnNames = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLimitAssignment extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "limit_profile_id", nullable = false)
    private LimitProfile limitProfile;
}
