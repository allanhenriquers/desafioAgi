package org.cadastro.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.dto.EnderecoDTO;
import org.cadastro.cliente.domain.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private EnderecoDTO getEndereco() {
        return new EnderecoDTO("Rua A", "123", "Apto 1", "Cidade X", "SP", "12345678");
    }

    private ClienteRequestDTO getClienteRequestDTO() {
        return new ClienteRequestDTO("12345678900", "Allan", LocalDate.of(1990, 1, 1), "999999999", getEndereco());
    }

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        ClienteRequestDTO cliente = getClienteRequestDTO();
        ClienteResponseDTO clienteResponseDTO = getClienteResponseDTO(cliente);

        when(clienteService.cadastrar(any(ClienteRequestDTO.class))).thenReturn(clienteResponseDTO);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value(cliente.getCpf()));
    }

    @Test
    void deveBuscarClientePorCpf() throws Exception {
        ClienteRequestDTO clienteRequestDTO = getClienteRequestDTO();
        ClienteResponseDTO clienteResponseDTO = getClienteResponseDTO(clienteRequestDTO);

        when(clienteService.buscarPorCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.of(clienteResponseDTO));

        mockMvc.perform(get("/clientes/" + clienteRequestDTO.getCpf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(clienteRequestDTO.getNome()));
    }

    @Test
    void deveRetornar404SeClienteNaoEncontrado() throws Exception {
        when(clienteService.buscarPorCpf("000")).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        ClienteRequestDTO cliente = getClienteRequestDTO();
        ClienteResponseDTO clienteResponseDTO = getClienteResponseDTO(cliente);
        when(clienteService.atualizar(eq(cliente.getCpf()), any(ClienteRequestDTO.class)))
                .thenReturn(clienteResponseDTO);

        mockMvc.perform(put("/clientes/" + cliente.getCpf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(cliente.getCpf()));
    }

    private static ClienteResponseDTO getClienteResponseDTO(ClienteRequestDTO cliente) {
        return ClienteResponseDTO.builder()
                .cpf(cliente.getCpf())
                .nome("Allan")
                .build();
    }

    @Test
    void deveRemoverCliente() throws Exception {
        mockMvc.perform(delete("/clientes/12345678900"))
                .andExpect(status().isNoContent());
        verify(clienteService).remover("12345678900");
    }
}