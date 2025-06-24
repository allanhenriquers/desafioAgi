package org.cadastro.cliente.service.impl;

import lombok.RequiredArgsConstructor;
import org.cadastro.cliente.excecao.ClienteExistenteException;
import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.repository.ClienteRepository;
import org.cadastro.cliente.excecao.ClienteNaoEncontradoException;
import org.cadastro.cliente.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    @Override
    public Cliente cadastrar(Cliente cliente) throws ClienteExistenteException {
        if (repository.existsById(cliente.getCpf())) {
            throw new ClienteExistenteException("Cliente com CPF já cadastrado");
        }
        return repository.save(cliente);
    }

    @Override
    public Cliente atualizar(String cpf, Cliente cliente) {
        Cliente existente = repository.findById(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
        existente.setNome(cliente.getNome());
        existente.setDataNascimento(cliente.getDataNascimento());
        existente.setTelefone(cliente.getTelefone());
        existente.setEndereco(cliente.getEndereco());
        return repository.save(existente);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return repository.findById(cpf);
    }

    @Override
    public void remover(String cpf) {
        if (!repository.existsById(cpf)) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado");
        }
        repository.deleteById(cpf);
    }
}
