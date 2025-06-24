package org.contratacao.seguro.domain.model.repository;

import org.contratacao.seguro.domain.model.Contratacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeguroRepository extends JpaRepository<Contratacao, UUID> {
}
