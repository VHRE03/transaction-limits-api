package com.vhre.transactionlimitsengine.modules.limitprofile.controller;

import com.vhre.base.core.base.controller.BaseController;
import com.vhre.transactionlimitsengine.modules.limitprofile.dto.LimitProfileDTO;
import com.vhre.transactionlimitsengine.modules.limitprofile.entity.LimitProfile;
import com.vhre.transactionlimitsengine.modules.limitprofile.service.LimitProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/limit-profiles")
@Tag(name = "Limit Profile Management", description = "Endpoints for managing Limit Profile")
public class LimitProfileController extends BaseController<LimitProfile, LimitProfileDTO, UUID> {

    public LimitProfileController(LimitProfileService service) {
        super(service);
    }
}
