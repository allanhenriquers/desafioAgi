package org.contratacao.seguro.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.contratacao.seguro.client.CustomerClient;
import org.contratacao.seguro.client.model.Cliente;
import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.domain.model.Simulacao;
import org.contratacao.seguro.domain.model.TipoSeguro;
import org.contratacao.seguro.domain.model.repository.SeguroRepository;
import org.contratacao.seguro.excecao.ClienteNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeguroService {

    private final CustomerClient customerClient;
    private final SeguroRepository seguroRepository;

    @CircuitBreaker(name = "customerService", fallbackMethod = "fallbackSimulacao")
    public Simulacao simularSeguro(String cpf, TipoSeguro tipo) {
        ResponseEntity<Cliente> response = customerClient.buscarCliente(cpf);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado");
        }
        BigDecimal valor = calcularValorSeguro(tipo);
        return new Simulacao(cpf, tipo, valor);
    }

    public Simulacao fallbackSimulacao(String cpf, TipoSeguro tipo, Throwable t) {
        return new Simulacao(cpf, tipo, BigDecimal.ZERO);
    }

    @CircuitBreaker(name = "customerService")
    public Contratacao contratarSeguro(String cpf, TipoSeguro tipo) {
        ResponseEntity<Cliente> response = customerClient.buscarCliente(cpf);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado");
        }
        Contratacao contratacao = new Contratacao(UUID.randomUUID(), cpf, tipo, LocalDate.now());
        return seguroRepository.save(contratacao);
    }

    private BigDecimal calcularValorSeguro(TipoSeguro tipo) {
        return switch (tipo) {
            case BRONZE -> BigDecimal.valueOf(100);
            case PRATA -> BigDecimal.valueOf(200);
            case OURO -> BigDecimal.valueOf(300);
        };
    }
}
