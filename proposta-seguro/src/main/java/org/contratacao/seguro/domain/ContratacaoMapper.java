package org.contratacao.seguro.domain;

import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.dto.ContratacaoResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContratacaoMapper {
    ContratacaoResponseDTO mapToResponse(Contratacao contratacao);
}
