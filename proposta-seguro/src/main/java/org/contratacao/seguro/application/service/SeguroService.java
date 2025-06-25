package org.contratacao.seguro.application.service;

import org.contratacao.seguro.domain.model.Simulacao;
import org.contratacao.seguro.domain.model.TipoSeguro;
import org.contratacao.seguro.dto.ContratacaoResponseDTO;

import java.math.BigDecimal;

public interface SeguroService {
    Simulacao simularSeguro(String cpf, TipoSeguro tipo);

    Simulacao fallbackSimulacao(String cpf, TipoSeguro tipo, Throwable t);

    ContratacaoResponseDTO contratarSeguro(String cpf, TipoSeguro tipo);

    BigDecimal calcularValorSeguro(TipoSeguro tipo);
}
