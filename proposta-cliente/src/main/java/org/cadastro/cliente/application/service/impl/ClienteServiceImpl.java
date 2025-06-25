package org.cadastro.cliente.application.service.impl;

import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.port.ClienteRepositoryPort;
import org.cadastro.cliente.application.service.ClienteService;
import org.cadastro.cliente.application.service.mapper.ClienteMapper;
import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.infra.ClienteExistenteException;
import org.cadastro.cliente.infra.ClienteNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final ClienteMapper mapper;

    public ClienteServiceImpl(ClienteRepositoryPort clienteRepositoryPort, ClienteMapper mapper) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    public ClienteResponseDTO cadastrar(ClienteRequestDTO cliente) throws ClienteExistenteException {
        if (clienteRepositoryPort.existsByCpf(cliente.getCpf())) {
            throw new ClienteExistenteException("Cliente com CPF já cadastrado");
        }

        return mapper.mapToResponse(clienteRepositoryPort.save(mapper.mapToCliente(cliente)));
    }

    @Override
    public ClienteResponseDTO atualizar(String cpf, ClienteRequestDTO cliente) {
        Cliente existente = clienteRepositoryPort.findByCpf(cpf).orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
        existente.setNome(cliente.getNome());
        existente.setDataNascimento(cliente.getDataNascimento());
        existente.setTelefone(cliente.getTelefone());
        existente.setEndereco(mapper.mapEndereco(cliente.getEndereco()));
        return mapper.mapToResponse(clienteRepositoryPort.save(existente));
    }

    @Override
    public Optional<ClienteResponseDTO> buscarPorCpf(String cpf) {
        return clienteRepositoryPort.findByCpf(cpf).map(mapper::mapToResponse);
    }

    @Override
    public void remover(String cpf) {
        if (!clienteRepositoryPort.existsByCpf(cpf)) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado");
        }
        clienteRepositoryPort.deleteByCpf(cpf);
    }
}
