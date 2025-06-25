package org.cadastro.cliente.domain.service.impl;

import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.repository.ClienteRepository;
import org.cadastro.cliente.domain.service.ClienteService;
import org.cadastro.cliente.domain.service.mapper.ClienteMapper;
import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.excecao.ClienteExistenteException;
import org.cadastro.cliente.excecao.ClienteNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    public ClienteServiceImpl(ClienteRepository repository, ClienteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ClienteResponseDTO cadastrar(ClienteRequestDTO cliente) throws ClienteExistenteException {
        if (repository.existsById(cliente.getCpf())) {
            throw new ClienteExistenteException("Cliente com CPF já cadastrado");
        }

        return mapper.mapToResponse(repository.save(mapper.mapToCliente(cliente)));
    }

    @Override
    public ClienteResponseDTO atualizar(String cpf, ClienteRequestDTO cliente) {
        Cliente existente = repository.findById(cpf).orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
        existente.setNome(cliente.getNome());
        existente.setDataNascimento(cliente.getDataNascimento());
        existente.setTelefone(cliente.getTelefone());
        existente.setEndereco(mapper.mapEndereco(cliente.getEndereco()));
        return mapper.mapToResponse(repository.save(existente));
    }

    @Override
    public Optional<ClienteResponseDTO> buscarPorCpf(String cpf) {
        return repository.findById(cpf).map(mapper::mapToResponse);
    }

    @Override
    public void remover(String cpf) {
        if (!repository.existsById(cpf)) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado");
        }
        repository.deleteById(cpf);
    }
}
