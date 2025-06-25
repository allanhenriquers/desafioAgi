package org.cadastro.cliente.application.service;

import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.infra.ClienteExistenteException;

import java.util.Optional;

public interface ClienteService {
    ClienteResponseDTO cadastrar(ClienteRequestDTO cliente) throws ClienteExistenteException;

    ClienteResponseDTO atualizar(String cpf, ClienteRequestDTO cliente);

    Optional<ClienteResponseDTO> buscarPorCpf(String cpf);

    void remover(String cpf);
}