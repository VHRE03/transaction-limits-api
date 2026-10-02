package com.vhre.transactionlimitsengine.modules.userlimitassignment.service;

import com.vhre.base.core.base.service.BaseServiceImpl;
import com.vhre.base.core.exceptions.ResourceNotFoundException;
import com.vhre.transactionlimitsengine.modules.limitprofile.entity.LimitProfile;
import com.vhre.transactionlimitsengine.modules.limitprofile.repository.LimitProfileRepository;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.dto.UserLimitAssignmentDTO;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.entity.UserLimitAssignment;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.mapper.UserLimitAssignmentMapper;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.repository.UserLimitAssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserLimitAssignmentServiceImpl extends BaseServiceImpl<UserLimitAssignment, UserLimitAssignmentDTO, UUID>
        implements UserLimitAssignmentService {

    private final LimitProfileRepository limitProfileRepository;

    public UserLimitAssignmentServiceImpl(UserLimitAssignmentRepository repository,
                                           UserLimitAssignmentMapper mapper,
                                           LimitProfileRepository limitProfileRepository) {
        super(repository, mapper);
        this.limitProfileRepository = limitProfileRepository;
    }

    @Override
    public UserLimitAssignmentDTO save(UserLimitAssignmentDTO dto) {
        UserLimitAssignment entity = mapper.toEntity(dto);

        applyLimitProfile(dto.getLimitProfileId(), entity);

        UserLimitAssignment saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public UserLimitAssignmentDTO update(UUID id, UserLimitAssignmentDTO dto) {
        UserLimitAssignment entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserLimitAssignment not found with id: " + id));

        mapper.updateEntityFromDto(dto, entity);
        applyLimitProfile(dto.getLimitProfileId(), entity);

        UserLimitAssignment saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    private void applyLimitProfile(UUID limitProfileId, UserLimitAssignment entity) {
        if (limitProfileId == null) {
            return;
        }
        LimitProfile limitProfile = limitProfileRepository.findById(limitProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("LimitProfile not found with id: " + limitProfileId));
        entity.setLimitProfile(limitProfile);
    }
}
