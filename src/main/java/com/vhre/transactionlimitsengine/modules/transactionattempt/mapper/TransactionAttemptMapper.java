package com.vhre.transactionlimitsengine.modules.transactionattempt.mapper;

import com.vhre.base.core.base.mapper.BaseMapper;
import com.vhre.transactionlimitsengine.modules.transactionattempt.dto.TransactionAttemptDTO;
import com.vhre.transactionlimitsengine.modules.transactionattempt.entity.TransactionAttempt;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface TransactionAttemptMapper extends BaseMapper<TransactionAttempt, TransactionAttemptDTO> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(TransactionAttemptDTO dto, @MappingTarget TransactionAttempt entity);
}
