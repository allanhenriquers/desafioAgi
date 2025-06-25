package org.contratacao.seguro.application.service;

import org.contratacao.seguro.domain.ContratacaoMapper;
import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.domain.model.Simulacao;
import org.contratacao.seguro.domain.model.TipoSeguro;
import org.contratacao.seguro.domain.port.ClientePort;
import org.contratacao.seguro.domain.port.ContratacaoRepositoryPort;
import org.contratacao.seguro.dto.ContratacaoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeguroServiceImplTest {

    @Mock
    private ClientePort clientePort;

    @Mock
    private ContratacaoRepositoryPort contratacaoRepositoryPort;

    @Mock
    private ContratacaoMapper contratacaoMapper;

    @InjectMocks
    private SeguroServiceImpl seguroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seguroService = new SeguroServiceImpl(clientePort, contratacaoRepositoryPort, contratacaoMapper);
    }

    @Test
    void deveRetornarSimulacaoComValorCorreto() {
        String cpf = "12345678901";
        TipoSeguro tipo = TipoSeguro.PRATA;

        doNothing().when(clientePort).buscarCliente(cpf);

        Simulacao simulacao = seguroService.simularSeguro(cpf, tipo);

        assertNotNull(simulacao);
        assertEquals(cpf, simulacao.getClienteCpf());
        assertEquals(tipo, simulacao.getTipo());
        assertEquals(BigDecimal.valueOf(200), simulacao.getValor());

        verify(clientePort, times(1)).buscarCliente(cpf);
    }

    @Test
    void devePropagarExcecaotestAoSimularSeguroQuandoClienteNaoForEncontrado() {
        String cpf = "99999999999";
        TipoSeguro tipo = TipoSeguro.BRONZE;

        doThrow(new RuntimeException("Cliente não encontrado")).when(clientePort).buscarCliente(cpf);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            seguroService.simularSeguro(cpf, tipo);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clientePort, times(1)).buscarCliente(cpf);
    }

    @Test
    void deveRetornarSimulacaoComValorZeroFallbackSimulacao() {
        String cpf = "12345678901";
        TipoSeguro tipo = TipoSeguro.OURO;
        Throwable t = new RuntimeException("Erro simulado");

        Simulacao fallback = seguroService.fallbackSimulacao(cpf, tipo, t);

        assertNotNull(fallback);
        assertEquals(cpf, fallback.getClienteCpf());
        assertEquals(tipo, fallback.getTipo());
        assertEquals(BigDecimal.ZERO, fallback.getValor());
    }

    @Test
    void deveSalvarContratacaoERetornarDTOAoContratarSeguro() {
        String cpf = "12345678901";
        TipoSeguro tipo = TipoSeguro.BRONZE;

        doNothing().when(clientePort).buscarCliente(cpf);

        Contratacao contratacaoSalva = new Contratacao(UUID.randomUUID(), cpf, tipo, LocalDate.now());
        ContratacaoResponseDTO responseDTO = new ContratacaoResponseDTO(cpf, tipo, LocalDate.now());

        when(contratacaoRepositoryPort.save(any(Contratacao.class))).thenReturn(contratacaoSalva);
        when(contratacaoMapper.mapToResponse(contratacaoSalva)).thenReturn(responseDTO);

        ContratacaoResponseDTO resultado = seguroService.contratarSeguro(cpf, tipo);

        assertNotNull(resultado);
        assertEquals(cpf, resultado.getClienteCpf());
        assertEquals(tipo, resultado.getTipo());
        assertNotNull(resultado.getDataContratacao());

        verify(clientePort, times(1)).buscarCliente(cpf);
        verify(contratacaoRepositoryPort, times(1)).save(any(Contratacao.class));
        verify(contratacaoMapper, times(1)).mapToResponse(contratacaoSalva);
    }

    @Test
    void ddevePropagarExcecaoAoContratarSeguroEClienteNaoForEncontrado() {
        String cpf = "00000000000";
        TipoSeguro tipo = TipoSeguro.OURO;

        doThrow(new RuntimeException("Cliente não encontrado")).when(clientePort).buscarCliente(cpf);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            seguroService.contratarSeguro(cpf, tipo);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clientePort, times(1)).buscarCliente(cpf);
        verifyNoInteractions(contratacaoRepositoryPort);
        verifyNoInteractions(contratacaoMapper);
    }

    @Test
    void deveRetornarValorCorretoParaCadaTipoCalcularValorSeguro() {
        assertEquals(BigDecimal.valueOf(100), seguroService.calcularValorSeguro(TipoSeguro.BRONZE));
        assertEquals(BigDecimal.valueOf(200), seguroService.calcularValorSeguro(TipoSeguro.PRATA));
        assertEquals(BigDecimal.valueOf(300), seguroService.calcularValorSeguro(TipoSeguro.OURO));
    }
}
