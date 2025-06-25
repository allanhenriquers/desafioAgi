package org.contratacao.seguro.infra.adapter;

import org.contratacao.seguro.client.CustomerClient;
import org.contratacao.seguro.domain.model.Cliente;
import org.contratacao.seguro.domain.port.ClientePort;
import org.contratacao.seguro.infra.ClienteNaoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteAdapter implements ClientePort {

    private final CustomerClient customerClient;

    public ClienteAdapter(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }

    @Override
    public void buscarCliente(String cpf) {
        ResponseEntity<Cliente> response = customerClient.buscarCliente(cpf);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ClienteNaoEncontradoException("Cliente não encontrado");
        }
    }
}