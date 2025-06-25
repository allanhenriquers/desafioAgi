package org.contratacao.seguro.infra.adapter;

import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.domain.model.repository.SeguroRepository;
import org.contratacao.seguro.domain.port.ContratacaoRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class SeguroRepositoryAdapter implements ContratacaoRepositoryPort {

    private final SeguroRepository repository;

    public SeguroRepositoryAdapter(SeguroRepository repository) {
        this.repository = repository;
    }

    @Override
    public Contratacao save(Contratacao contratacao) {
        return repository.save(contratacao);
    }
}