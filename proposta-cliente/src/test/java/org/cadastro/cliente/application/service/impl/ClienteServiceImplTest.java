package org.cadastro.cliente.application.service.impl;

import org.cadastro.cliente.application.service.mapper.ClienteMapper;
import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.port.ClienteRepositoryPort;
import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.infra.ClienteExistenteException;
import org.cadastro.cliente.infra.ClienteNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceImplTest {

    @Mock
    private ClienteRepositoryPort clienteRepositoryPort;

    @Mock
    private ClienteMapper mapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteRequestDTO clienteRequestDTO;
    private Cliente cliente;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteRequestDTO = ClienteRequestDTO.builder()
                .cpf("12345678901")
                .nome("Fulano")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .telefone("11999999999")
                .endereco(null) // pode ajustar conforme DTO
                .build();

        cliente = Cliente.builder()
                .cpf("12345678901")
                .nome("Fulano")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .telefone("11999999999")
                .endereco(null)
                .build();

        clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setCpf("12345678901");
        clienteResponseDTO.setNome("Fulano");
        clienteResponseDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        clienteResponseDTO.setTelefone("11999999999");
        clienteResponseDTO.setEndereco(null);
    }

    @Test
    void deveCadastrarClienteComSucessoQuandoCpfNaoExistir() {
        when(clienteRepositoryPort.existsByCpf(clienteRequestDTO.getCpf())).thenReturn(false);
        when(mapper.mapToCliente(clienteRequestDTO)).thenReturn(cliente);
        when(clienteRepositoryPort.save(cliente)).thenReturn(cliente);
        when(mapper.mapToResponse(cliente)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resultado = clienteService.cadastrar(clienteRequestDTO);

        assertNotNull(resultado);
        assertEquals(clienteRequestDTO.getCpf(), resultado.getCpf());
        verify(clienteRepositoryPort, times(1)).existsByCpf(clienteRequestDTO.getCpf());
        verify(clienteRepositoryPort, times(1)).save(cliente);
        verify(mapper, times(1)).mapToCliente(clienteRequestDTO);
        verify(mapper, times(1)).mapToResponse(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoCadastrarClienteComCpfExistente() {
        when(clienteRepositoryPort.existsByCpf(clienteRequestDTO.getCpf())).thenReturn(true);

        ClienteExistenteException exception = assertThrows(ClienteExistenteException.class, () -> {
            clienteService.cadastrar(clienteRequestDTO);
        });

        assertEquals("Cliente com CPF já cadastrado", exception.getMessage());
        verify(clienteRepositoryPort, times(1)).existsByCpf(clienteRequestDTO.getCpf());
        verify(clienteRepositoryPort, never()).save(any());
        verify(mapper, never()).mapToCliente(any());
        verify(mapper, never()).mapToResponse(any());
    }

    @Test
    void deveAtualizarClienteComSucessoQuandoClienteExistir() {
        when(clienteRepositoryPort.findByCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.of(cliente));
        when(mapper.mapEndereco(clienteRequestDTO.getEndereco())).thenReturn(null);
        when(clienteRepositoryPort.save(cliente)).thenReturn(cliente);
        when(mapper.mapToResponse(cliente)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resultado = clienteService.atualizar(clienteRequestDTO.getCpf(), clienteRequestDTO);

        assertNotNull(resultado);
        assertEquals(clienteRequestDTO.getCpf(), resultado.getCpf());
        verify(clienteRepositoryPort, times(1)).findByCpf(clienteRequestDTO.getCpf());
        verify(mapper, times(1)).mapEndereco(clienteRequestDTO.getEndereco());
        verify(clienteRepositoryPort, times(1)).save(cliente);
        verify(mapper, times(1)).mapToResponse(cliente);
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarClienteNaoExistente() {
        when(clienteRepositoryPort.findByCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.empty());

        ClienteNaoEncontradoException exception = assertThrows(ClienteNaoEncontradoException.class, () -> {
            clienteService.atualizar(clienteRequestDTO.getCpf(), clienteRequestDTO);
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepositoryPort, times(1)).findByCpf(clienteRequestDTO.getCpf());
        verify(clienteRepositoryPort, never()).save(any());
        verify(mapper, never()).mapEndereco(any());
        verify(mapper, never()).mapToResponse(any());
    }

    @Test
    void deveRetornarClienteResponseQuandoBuscarPorCpfExistente() {
        when(clienteRepositoryPort.findByCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.of(cliente));
        when(mapper.mapToResponse(cliente)).thenReturn(clienteResponseDTO);

        Optional<ClienteResponseDTO> resultado = clienteService.buscarPorCpf(clienteRequestDTO.getCpf());

        assertTrue(resultado.isPresent());
        assertEquals(clienteRequestDTO.getCpf(), resultado.get().getCpf());
        verify(clienteRepositoryPort, times(1)).findByCpf(clienteRequestDTO.getCpf());
        verify(mapper, times(1)).mapToResponse(cliente);
    }

    @Test
    void deveRetornarOptionalVazioQuandoBuscarPorCpfInexistente() {
        when(clienteRepositoryPort.findByCpf(clienteRequestDTO.getCpf())).thenReturn(Optional.empty());

        Optional<ClienteResponseDTO> resultado = clienteService.buscarPorCpf(clienteRequestDTO.getCpf());

        assertFalse(resultado.isPresent());
        verify(clienteRepositoryPort, times(1)).findByCpf(clienteRequestDTO.getCpf());
        verify(mapper, never()).mapToResponse(any());
    }

    @Test
    void deveRemoverClienteComSucessoQuandoCpfExistir() {
        when(clienteRepositoryPort.existsByCpf(clienteRequestDTO.getCpf())).thenReturn(true);
        doNothing().when(clienteRepositoryPort).deleteByCpf(clienteRequestDTO.getCpf());

        assertDoesNotThrow(() -> clienteService.remover(clienteRequestDTO.getCpf()));

        verify(clienteRepositoryPort, times(1)).existsByCpf(clienteRequestDTO.getCpf());
        verify(clienteRepositoryPort, times(1)).deleteByCpf(clienteRequestDTO.getCpf());
    }

    @Test
    void deveLancarExcecaoQuandoRemoverClienteComCpfInexistente() {
        when(clienteRepositoryPort.existsByCpf(clienteRequestDTO.getCpf())).thenReturn(false);

        ClienteNaoEncontradoException exception = assertThrows(ClienteNaoEncontradoException.class, () -> {
            clienteService.remover(clienteRequestDTO.getCpf());
        });

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(clienteRepositoryPort, times(1)).existsByCpf(clienteRequestDTO.getCpf());
        verify(clienteRepositoryPort, never()).deleteByCpf(any());
    }
}
