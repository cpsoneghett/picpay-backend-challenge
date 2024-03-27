package com.cpsoneghett.api.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface TransactionMapper {

    @Mapping(target = "id", source = "dto.idTransaction")
    @Mapping(target = "payer.id", source = "dto.payerId")
    @Mapping(target = "payee.id", source = "dto.payeeId")
    Transaction dtoToModel(TransactionDTO dto);

    @Mapping(target = "idTransaction", source = "model.id")
    @Mapping(target = "payerId", source = "model.payer.id")
    @Mapping(target = "payeeId", source = "model.payee.id")
    TransactionDTO modelToDto(Transaction model);

    List<TransactionDTO> map(List<Transaction> employees);
}
