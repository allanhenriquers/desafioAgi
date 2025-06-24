package org.cadastro.cliente.domain.repository;


import org.cadastro.cliente.domain.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

}