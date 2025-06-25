package org.cadastro.cliente.domain.port;

import org.cadastro.cliente.domain.model.Cliente;

import java.util.Optional;

public interface ClienteRepositoryPort {
    boolean existsByCpf(String cpf);

    Cliente save(Cliente cliente);

    Optional<Cliente> findByCpf(String cpf);

    void deleteByCpf(String cpf);
}
