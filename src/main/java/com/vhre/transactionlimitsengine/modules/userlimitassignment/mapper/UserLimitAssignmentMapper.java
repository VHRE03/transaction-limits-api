package com.vhre.transactionlimitsengine.modules.userlimitassignment.mapper;

import com.vhre.base.core.base.mapper.BaseMapper;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.dto.UserLimitAssignmentDTO;
import com.vhre.transactionlimitsengine.modules.userlimitassignment.entity.UserLimitAssignment;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserLimitAssignmentMapper extends BaseMapper<UserLimitAssignment, UserLimitAssignmentDTO> {

    @Override
    @Mapping(target = "limitProfile", ignore = true)
    UserLimitAssignment toEntity(UserLimitAssignmentDTO dto);

    @Override
    @Mapping(target = "limitProfile", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(UserLimitAssignmentDTO dto, @MappingTarget UserLimitAssignment entity);

    @Override
    @Mapping(target = "limitProfileId", source = "limitProfile.id")
    UserLimitAssignmentDTO toDto(UserLimitAssignment entity);
}
