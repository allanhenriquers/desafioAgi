package org.contratacao.seguro.domain.port;

import org.contratacao.seguro.domain.model.Contratacao;

public interface ContratacaoRepositoryPort {
    Contratacao save(Contratacao contratacao);
}