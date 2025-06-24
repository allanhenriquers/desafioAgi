package org.cadastro.cliente.service;

import org.cadastro.cliente.excecao.ClienteExistenteException;
import org.cadastro.cliente.domain.model.Cliente;

import java.util.Optional;

public interface ClienteService {
    Cliente cadastrar(Cliente cliente) throws ClienteExistenteException;

    Cliente atualizar(String id, Cliente cliente);

    Optional<Cliente> buscarPorCpf(String id);

    void remover(String id);
}