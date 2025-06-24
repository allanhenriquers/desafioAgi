package org.cadastro.cliente.service.impl;

import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.model.Endereco;
import org.cadastro.cliente.domain.repository.ClienteRepository;
import org.cadastro.cliente.excecao.ClienteExistenteException;
import org.cadastro.cliente.excecao.ClienteNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteServiceImpl service;

    private Cliente cliente;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        endereco = new Endereco("Rua A", "123", "Bairro A", "Cidade A", "Estado A", "12345-678");
        cliente = new Cliente();
        cliente.setCpf("12345678900");
        cliente.setNome("João da Silva");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("11999999999");
        cliente.setEndereco(endereco);
    }

    @Test
    void deveCadastrarClienteQuandoCpfNaoExiste() {
        when(repository.existsById(cliente.getCpf())).thenReturn(false);
        when(repository.save(cliente)).thenReturn(cliente);

        Cliente salvo = service.cadastrar(cliente);

        assertEquals(cliente, salvo);
        verify(repository).save(cliente);
    }

    @Test
    void deveLancarExcecaoAoCadastrarClienteComCpfExistente() {
        when(repository.existsById(cliente.getCpf())).thenReturn(true);

        assertThrows(ClienteExistenteException.class, () -> service.cadastrar(cliente));
        verify(repository, never()).save(any());
    }

    @Test
    void deveAtualizarClienteExistente() {
        when(repository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));
        when(repository.save(any())).thenReturn(cliente);

        Cliente atualizado = new Cliente();
        atualizado.setNome("João Atualizado");
        atualizado.setDataNascimento(LocalDate.of(1985, 5, 5));
        atualizado.setTelefone("11888888888");
        atualizado.setEndereco(endereco);

        Cliente result = service.atualizar(cliente.getCpf(), atualizado);

        assertEquals("João Atualizado", result.getNome());
        verify(repository).save(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarClienteInexistente() {
        when(repository.findById("00000000000")).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class,
                () -> service.atualizar("00000000000", cliente));
    }

    @Test
    void deveBuscarClientePorCpf() {
        when(repository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));

        Optional<Cliente> encontrado = service.buscarPorCpf(cliente.getCpf());

        assertTrue(encontrado.isPresent());
        assertEquals(cliente.getCpf(), encontrado.get().getCpf());
    }

    @Test
    void deveRemoverClienteExistente() {
        when(repository.existsById(cliente.getCpf())).thenReturn(true);

        service.remover(cliente.getCpf());

        verify(repository).deleteById(cliente.getCpf());
    }

    @Test
    void deveLancarExcecaoAoRemoverClienteInexistente() {
        when(repository.existsById(cliente.getCpf())).thenReturn(false);

        assertThrows(ClienteNaoEncontradoException.class,
                () -> service.remover(cliente.getCpf()));
    }
}