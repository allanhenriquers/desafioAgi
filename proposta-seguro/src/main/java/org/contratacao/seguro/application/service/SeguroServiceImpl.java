package org.contratacao.seguro.application.service;

import org.contratacao.seguro.domain.ContratacaoMapper;
import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.domain.model.Simulacao;
import org.contratacao.seguro.domain.model.TipoSeguro;
import org.contratacao.seguro.domain.port.ClientePort;
import org.contratacao.seguro.domain.port.ContratacaoRepositoryPort;
import org.contratacao.seguro.dto.ContratacaoResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class SeguroServiceImpl implements SeguroService {

    private final ClientePort clientePort;
    private final ContratacaoRepositoryPort contratacaoRepositoryPort;
    private final ContratacaoMapper contratacaoMapper;

    public SeguroServiceImpl(ClientePort clientePort, ContratacaoRepositoryPort contratacaoRepositoryPort,
                             ContratacaoMapper contratacaoMapper) {
        this.clientePort = clientePort;
        this.contratacaoRepositoryPort = contratacaoRepositoryPort;
        this.contratacaoMapper = contratacaoMapper;
    }

    @Override
    public Simulacao simularSeguro(String cpf, TipoSeguro tipo) {
        clientePort.buscarCliente(cpf);
        BigDecimal valor = calcularValorSeguro(tipo);
        return new Simulacao(cpf, tipo, valor);
    }

    @Override
    public Simulacao fallbackSimulacao(String cpf, TipoSeguro tipo, Throwable t) {
        return new Simulacao(cpf, tipo, BigDecimal.ZERO);
    }

    @Override
    public ContratacaoResponseDTO contratarSeguro(String cpf, TipoSeguro tipo) {
        clientePort.buscarCliente(cpf);
        Contratacao contratacao = new Contratacao(UUID.randomUUID(), cpf, tipo, LocalDate.now());
        return contratacaoMapper.mapToResponse(contratacaoRepositoryPort.save(contratacao));
    }

    @Override
    public BigDecimal calcularValorSeguro(TipoSeguro tipo) {
        return switch (tipo) {
            case BRONZE -> BigDecimal.valueOf(100);
            case PRATA -> BigDecimal.valueOf(200);
            case OURO -> BigDecimal.valueOf(300);
        };
    }
}
