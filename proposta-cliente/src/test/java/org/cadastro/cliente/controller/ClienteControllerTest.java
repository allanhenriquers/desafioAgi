package org.cadastro.cliente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.model.Endereco;
import org.cadastro.cliente.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private Endereco getEndereco() {
        return new Endereco("Rua A", "123", "Apto 1", "Cidade X", "SP", "12345678");
    }

    private Cliente getCliente() {
        return new Cliente("12345678900", "Allan", LocalDate.of(1990, 1, 1), "999999999", getEndereco());
    }

    @Test
    void deveCriarClienteComSucesso() throws Exception {
        Cliente cliente = getCliente();

        Mockito.when(clienteService.cadastrar(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value(cliente.getCpf()));
    }

    @Test
    void deveBuscarClientePorCpf() throws Exception {
        Cliente cliente = getCliente();

        Mockito.when(clienteService.buscarPorCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/" + cliente.getCpf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(cliente.getNome()));
    }

    @Test
    void deveRetornar404SeClienteNaoEncontrado() throws Exception {
        Mockito.when(clienteService.buscarPorCpf("000")).thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        Cliente cliente = getCliente();
        Mockito.when(clienteService.atualizar(eq(cliente.getCpf()), any(Cliente.class)))
                .thenReturn(cliente);

        mockMvc.perform(put("/clientes/" + cliente.getCpf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value(cliente.getCpf()));
    }

    @Test
    void deveRemoverCliente() throws Exception {
        mockMvc.perform(delete("/clientes/12345678900"))
                .andExpect(status().isNoContent());
        Mockito.verify(clienteService).remover("12345678900");
    }
}