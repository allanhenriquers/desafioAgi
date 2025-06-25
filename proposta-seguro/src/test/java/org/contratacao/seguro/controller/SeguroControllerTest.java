package org.contratacao.seguro.controller;

import org.contratacao.seguro.application.service.SeguroService;
import org.contratacao.seguro.domain.model.Simulacao;
import org.contratacao.seguro.domain.model.TipoSeguro;
import org.contratacao.seguro.dto.ContratacaoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeguroControllerTest {

    @Mock
    private SeguroService seguroService;

    @InjectMocks
    private SeguroController seguroController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seguroController = new SeguroController(seguroService);
    }

    @Test
    void deveRetornarSimulacaoComStatusOkQuandoSimularSeguro() {
        String cpf = "12345678901";
        TipoSeguro tipo = TipoSeguro.BRONZE;
        Simulacao simulacaoMock = new Simulacao(cpf, tipo, BigDecimal.valueOf(1500.00));

        when(seguroService.simularSeguro(cpf, tipo)).thenReturn(simulacaoMock);

        ResponseEntity<Simulacao> response = seguroController.simularSeguro(cpf, tipo);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(simulacaoMock, response.getBody());
        verify(seguroService, times(1)).simularSeguro(cpf, tipo);
    }

    @Test
    void deveRetornarContratacaoResponseComStatusCreatedQuandoContratarSeguro() {
        String cpf = "12345678901";
        TipoSeguro tipo = TipoSeguro.PRATA;
        ContratacaoResponseDTO responseDTO = new ContratacaoResponseDTO(cpf, tipo, LocalDate.now());

        when(seguroService.contratarSeguro(cpf, tipo)).thenReturn(responseDTO);

        ResponseEntity<ContratacaoResponseDTO> response = seguroController.contratarSeguro(cpf, tipo);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(seguroService, times(1)).contratarSeguro(cpf, tipo);
    }

       @Test
    void deveLancarExcecaoQuandoSimularSeguroComCpfInvalido() {
        String cpf = "cpf-invalido";
        TipoSeguro tipo = TipoSeguro.OURO;

        when(seguroService.simularSeguro(cpf, tipo)).thenThrow(new IllegalArgumentException("CPF inválido"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            seguroController.simularSeguro(cpf, tipo);
        });

        assertEquals("CPF inválido", exception.getMessage());
        verify(seguroService, times(1)).simularSeguro(cpf, tipo);
    }

    @Test
    void deveLancarExcecaoQuandoContratarSeguroComTipoNulo() {
        String cpf = "12345678901";
        TipoSeguro tipo = null;

        when(seguroService.contratarSeguro(cpf, tipo)).thenThrow(new NullPointerException("Tipo de seguro não pode ser nulo"));

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            seguroController.contratarSeguro(cpf, tipo);
        });

        assertEquals("Tipo de seguro não pode ser nulo", exception.getMessage());
        verify(seguroService, times(1)).contratarSeguro(cpf, tipo);
    }

}
