package org.contratacao.seguro.client;

import org.contratacao.seguro.domain.model.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer", url = "${customer.api.url}")
public interface CustomerClient {

    @GetMapping("/clientes/{cpf}")
    ResponseEntity<Cliente> buscarCliente(@PathVariable String cpf);
}
