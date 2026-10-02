package com.vhre.transactionlimitsengine.modules.userlimitassignment.repository;

import com.vhre.transactionlimitsengine.modules.userlimitassignment.entity.UserLimitAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserLimitAssignmentRepository extends JpaRepository<UserLimitAssignment, UUID> {
}
