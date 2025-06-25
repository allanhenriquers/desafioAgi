package org.cadastro.cliente.domain.port;

import org.cadastro.cliente.domain.model.Cliente;
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
        return false;
    }

    @Override
    public Cliente save(Cliente cliente) {
        return null;
    }

    @Override
    public Optional<Cliente> findByCpf(String cpf) {
        return Optional.empty();
    }

    @Override
    public void deleteByCpf(String cpf) {

    }
}
