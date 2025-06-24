package org.cadastro.cliente.service.impl;

import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.repository.ClienteRepository;
import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.excecao.ClienteExistenteException;
import org.cadastro.cliente.excecao.ClienteNaoEncontradoException;
import org.cadastro.cliente.service.mapper.ClienteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private ClienteMapper mapper;

    @InjectMocks
    private ClienteServiceImpl service;

    private ClienteRequestDTO clienteRequest;
    private Cliente clienteEntity;
    private ClienteResponseDTO clienteResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteRequest = new ClienteRequestDTO();
        clienteRequest.setCpf("12345678901");
        clienteRequest.setNome("João");
        clienteRequest.setDataNascimento(LocalDate.of(1990, 1, 1));
        clienteRequest.setTelefone("999999999");

        clienteEntity = new Cliente();
        clienteEntity.setCpf(clienteRequest.getCpf());
        clienteEntity.setNome(clienteRequest.getNome());
        clienteEntity.setDataNascimento(clienteRequest.getDataNascimento());
        clienteEntity.setTelefone(clienteRequest.getTelefone());

        clienteResponse = new ClienteResponseDTO();
        clienteResponse.setCpf(clienteRequest.getCpf());
        clienteResponse.setNome(clienteRequest.getNome());
        clienteResponse.setDataNascimento(clienteRequest.getDataNascimento());
        clienteResponse.setTelefone(clienteRequest.getTelefone());
    }

    @Test
    void deveCadastrarClienteQuandoNaoExistir() throws ClienteExistenteException {
        when(repository.existsById(clienteRequest.getCpf())).thenReturn(false);
        when(mapper.mapToCliente(clienteRequest)).thenReturn(clienteEntity);
        when(repository.save(clienteEntity)).thenReturn(clienteEntity);
        when(mapper.mapToResponse(clienteEntity)).thenReturn(clienteResponse);

        ClienteResponseDTO response = service.cadastrar(clienteRequest);

        assertNotNull(response);
        assertEquals(clienteRequest.getCpf(), response.getCpf());

        verify(repository).existsById(clienteRequest.getCpf());
        verify(repository).save(clienteEntity);
    }

    @Test
    void deveLancarExcecaoQuandoClienteExistir() {
        when(repository.existsById(clienteRequest.getCpf())).thenReturn(true);

        ClienteExistenteException ex = assertThrows(ClienteExistenteException.class, () -> {
            service.cadastrar(clienteRequest);
        });

        assertEquals("Cliente com CPF já cadastrado", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveAtualizarClienteQuandoExistir() {
        String cpf = clienteRequest.getCpf();
        when(repository.findById(cpf)).thenReturn(Optional.of(clienteEntity));
        when(mapper.mapEndereco(clienteRequest.getEndereco())).thenReturn(clienteEntity.getEndereco());
        when(repository.save(clienteEntity)).thenReturn(clienteEntity);
        when(mapper.mapToResponse(clienteEntity)).thenReturn(clienteResponse);

        ClienteResponseDTO response = service.atualizar(cpf, clienteRequest);

        assertNotNull(response);
        assertEquals(clienteRequest.getNome(), response.getNome());

        verify(repository).findById(cpf);
        verify(repository).save(clienteEntity);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        String cpf = clienteRequest.getCpf();
        when(repository.findById(cpf)).thenReturn(Optional.empty());

        ClienteNaoEncontradoException ex = assertThrows(ClienteNaoEncontradoException.class, () -> {
            service.atualizar(cpf, clienteRequest);
        });

        assertEquals("Cliente não encontrado", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveRetornarClienteQuandoExistir() {
        String cpf = clienteRequest.getCpf();
        when(repository.findById(cpf)).thenReturn(Optional.of(clienteEntity));
        when(mapper.mapToResponse(clienteEntity)).thenReturn(clienteResponse);

        Optional<ClienteResponseDTO> resultado = service.buscarPorCpf(cpf);

        assertTrue(resultado.isPresent());
        assertEquals(cpf, resultado.get().getCpf());

        verify(repository).findById(cpf);
    }

    @Test
    void deveRetornarVazioQuandoNaoExistir() {
        String cpf = clienteRequest.getCpf();
        when(repository.findById(cpf)).thenReturn(Optional.empty());

        Optional<ClienteResponseDTO> resultado = service.buscarPorCpf(cpf);

        assertFalse(resultado.isPresent());
        verify(repository).findById(cpf);
    }

    @Test
    void deveRemoverQuandoClienteExistir() {
        String cpf = clienteRequest.getCpf();
        when(repository.existsById(cpf)).thenReturn(true);
        doNothing().when(repository).deleteById(cpf);

        assertDoesNotThrow(() -> service.remover(cpf));

        verify(repository).existsById(cpf);
        verify(repository).deleteById(cpf);
    }

    @Test
    void deveLancarExcecaoAoTentarExcluirClienteQuandoClienteNaoExistir() {
        String cpf = clienteRequest.getCpf();
        when(repository.existsById(cpf)).thenReturn(false);

        ClienteNaoEncontradoException ex = assertThrows(ClienteNaoEncontradoException.class, () -> {
            service.remover(cpf);
        });

        assertEquals("Cliente não encontrado", ex.getMessage());
        verify(repository, never()).deleteById(any());
    }
}
