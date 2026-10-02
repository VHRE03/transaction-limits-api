package com.vhre.transactionlimitsengine.modules.userlimitassignment.controller;

import com.vhre.base.core.base.controller.BaseController;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.dto.UserLimitAssignmentDTO;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.entity.UserLimitAssignment;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.service.UserLimitAssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-limit-assignments")
@Tag(name = "User Limit Assignment Management", description = "Endpoints for managing User Limit Assignment")
public class UserLimitAssignmentController extends BaseController<UserLimitAssignment, UserLimitAssignmentDTO, UUID> {

    public UserLimitAssignmentController(UserLimitAssignmentService service) {
        super(service);
    }
}
