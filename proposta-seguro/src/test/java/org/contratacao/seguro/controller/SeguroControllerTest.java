package org.contratacao.seguro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.domain.model.Simulacao;
import org.contratacao.seguro.domain.model.TipoSeguro;
import org.contratacao.seguro.service.SeguroService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SeguroController.class)
class SeguroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeguroService seguroService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveSimularSeguroComSucesso() throws Exception {
        // Arrange
        String cpf = "12345678900";
        TipoSeguro tipo = TipoSeguro.BRONZE;
        Simulacao simulacao = new Simulacao(cpf, tipo, BigDecimal.valueOf(200.0));

        Mockito.when(seguroService.simularSeguro(eq(cpf), eq(tipo))).thenReturn(simulacao);

        // Act + Assert
        mockMvc.perform(post("/seguros/simular")
                        .param("cpf", cpf)
                        .param("tipo", tipo.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteCpf").value(cpf))
                .andExpect(jsonPath("$.tipo").value(tipo.name()))
                .andExpect(jsonPath("$.valor").value(200.0));
    }

    @Test
    void deveContratarSeguroComSucesso() throws Exception {
        // Arrange
        String cpf = "12345678900";
        TipoSeguro tipo = TipoSeguro.PRATA;
        LocalDate dataContratacao = LocalDate.now();
        Contratacao contratacao = new Contratacao(UUID.randomUUID(), cpf, tipo, dataContratacao);

        Mockito.when(seguroService.contratarSeguro(eq(cpf), eq(tipo))).thenReturn(contratacao);

        // Act + Assert
        mockMvc.perform(post("/seguros/contratar")
                        .param("cpf", cpf)
                        .param("tipo", tipo.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteCpf").value(cpf))
                .andExpect(jsonPath("$.tipo").value(tipo.name()))
                .andExpect(jsonPath("$.dataContratacao").value(dataContratacao.toString()));
    }
}
