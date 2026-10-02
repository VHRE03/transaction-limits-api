package com.vhre.transactionlimitsengine.modules.limitprofile.mapper;

import com.vhre.base.core.base.mapper.BaseMapper;
import com.vhre.transactionlimitsengine.modules.limitprofile.dto.LimitProfileDTO;
import com.vhre.transactionlimitsengine.modules.limitprofile.entity.LimitProfile;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface LimitProfileMapper extends BaseMapper<LimitProfile, LimitProfileDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(LimitProfileDTO dto, @MappingTarget LimitProfile entity);
}
