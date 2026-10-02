package com.vhre.transactionlimitsengine.modules.limitprofile.repository;

import com.vhre.transactionlimitsengine.modules.limitprofile.entity.LimitProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LimitProfileRepository extends JpaRepository<LimitProfile, UUID> {
}
