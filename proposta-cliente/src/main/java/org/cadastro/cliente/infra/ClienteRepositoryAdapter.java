package org.cadastro.cliente.infra;

import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.port.ClienteRepositoryPort;
import org.cadastro.cliente.domain.repository.ClienteRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepositoryPort {

    private final ClienteRepository repository;

    public ClienteRepositoryAdapter(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return repository.existsById(cpf);
    }

    @Override
    public Cliente save(Cliente cliente) {
        return repository.save(cliente);
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        return repository.findById(cpf);
    }

    @Override
    public void deleteByCpf(String cpf) {
        repository.deleteById(cpf);
    }
}
