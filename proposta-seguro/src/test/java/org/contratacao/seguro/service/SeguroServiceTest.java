package org.contratacao.seguro.service;

import org.contratacao.seguro.client.CustomerClient;
import org.contratacao.seguro.client.model.Cliente;
import org.contratacao.seguro.client.model.Endereco;
import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.domain.model.Simulacao;
import org.contratacao.seguro.domain.model.TipoSeguro;
import org.contratacao.seguro.domain.model.repository.SeguroRepository;
import org.contratacao.seguro.excecao.ClienteNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeguroServiceTest {

    @Mock
    private CustomerClient customerClient;

    @Mock
    private SeguroRepository seguroRepository;

    @InjectMocks
    private SeguroService seguroService;

    private final String cpfValido = "12345678901";

    private Cliente clienteExemplo;
    private Contratacao contratacaoExemplo;

    @BeforeEach
    void setup() {
        clienteExemplo = new Cliente();
        clienteExemplo.setCpf(cpfValido);
        clienteExemplo.setNome("Maria Silva");
        clienteExemplo.setDataNascimento(LocalDate.of(1985, 5, 20));
        clienteExemplo.setTelefone("11988887777");
        clienteExemplo.setEndereco(new Endereco("Rua B", "456", "Apto 10", "Rio de Janeiro", "RJ", "22041001"));

        contratacaoExemplo = new Contratacao(UUID.randomUUID(), cpfValido, TipoSeguro.PRATA, LocalDate.now());
    }

    @Test
    void deveRetornarUmaNovaSimulacaoQuandoClienteExistir() {
        when(customerClient.buscarCliente(cpfValido))
                .thenReturn(ResponseEntity.ok(clienteExemplo));

        Simulacao simulacao = seguroService.simularSeguro(cpfValido, TipoSeguro.PRATA);

        assertNotNull(simulacao);
        assertEquals(cpfValido, simulacao.getClienteCpf());
        assertEquals(TipoSeguro.PRATA, simulacao.getTipo());
        assertTrue(simulacao.getValor().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void deveLancarExcecaoAoSimularContratacaoQuandoOClienteNaoExistir() {
        when(customerClient.buscarCliente(cpfValido))
                .thenReturn(ResponseEntity.notFound().build());

        assertThrows(ClienteNaoEncontradoException.class,
                () -> seguroService.simularSeguro(cpfValido, TipoSeguro.BRONZE));
    }

    @Test
    void deveRetornarContatacaoQuandoExistirUmClienteComPerfilValido() {
        when(customerClient.buscarCliente(cpfValido))
                .thenReturn(ResponseEntity.ok(clienteExemplo));
        when(seguroRepository.save(any(Contratacao.class))).thenReturn(contratacaoExemplo);

        Contratacao contratacao = seguroService.contratarSeguro(cpfValido, TipoSeguro.PRATA);

        assertNotNull(contratacao);
        assertEquals(cpfValido, contratacao.getClienteCpf());
        assertEquals(TipoSeguro.PRATA, contratacao.getTipo());
        verify(seguroRepository, times(1)).save(any());
    }

    @Test
    void deveLancarExcecaoNaContratacaoQuandoClienteNaoExistir() {
        when(customerClient.buscarCliente(cpfValido))
                .thenReturn(ResponseEntity.notFound().build());

        assertThrows(ClienteNaoEncontradoException.class,
                () -> seguroService.contratarSeguro(cpfValido, TipoSeguro.OURO));
    }
}
