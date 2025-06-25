package org.cadastro.cliente.domain.service;

import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.excecao.ClienteExistenteException;
import org.cadastro.cliente.domain.model.Cliente;

import java.util.Optional;

public interface ClienteService {
    ClienteResponseDTO cadastrar(ClienteRequestDTO cliente) throws ClienteExistenteException;

    ClienteResponseDTO atualizar(String cpf, ClienteRequestDTO cliente);

    Optional<ClienteResponseDTO> buscarPorCpf(String cpf);

    void remover(String cpf);
}