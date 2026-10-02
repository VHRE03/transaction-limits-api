package com.vhre.transactionlimitsengine.modules.limitprofile.service;

import com.vhre.base.core.base.service.BaseServiceImpl;
import com.vhre.transactionlimitsengine.modules.limitprofile.dto.LimitProfileDTO;
import com.vhre.transactionlimitsengine.modules.limitprofile.entity.LimitProfile;
import com.vhre.transactionlimitsengine.modules.limitprofile.mapper.LimitProfileMapper;
import com.vhre.transactionlimitsengine.modules.limitprofile.repository.LimitProfileRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LimitProfileServiceImpl extends BaseServiceImpl<LimitProfile, LimitProfileDTO, UUID>
        implements LimitProfileService {

    public LimitProfileServiceImpl(LimitProfileRepository repository, LimitProfileMapper mapper) {
        super(repository, mapper);
    }
}
