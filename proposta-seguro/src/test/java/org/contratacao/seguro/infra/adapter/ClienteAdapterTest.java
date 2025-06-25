package org.contratacao.seguro.infra.adapter;

import org.contratacao.seguro.client.CustomerClient;
import org.contratacao.seguro.domain.model.Cliente;
import org.contratacao.seguro.infra.ClienteNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteAdapterTest {

    @Mock
    private CustomerClient customerClient;

    @InjectMocks
    private ClienteAdapter clienteAdapter;

    @Test
    void deveBuscarClienteComSucessoQuandoClienteExiste() {
        String cpf = "12345678901";
        Cliente cliente = new Cliente(cpf, "Fulano", null, null, null);
        ResponseEntity<Cliente> response = ResponseEntity.ok(cliente);

        when(customerClient.buscarCliente(cpf)).thenReturn(response);

        assertDoesNotThrow(() -> clienteAdapter.buscarCliente(cpf));
        verify(customerClient, times(1)).buscarCliente(cpf);
    }

    @Test
    void deveLancarExcecaoQuandoRespostaNaoEh2xx() {
        String cpf = "00000000000";
        ResponseEntity<Cliente> response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        when(customerClient.buscarCliente(cpf)).thenReturn(response);

        ClienteNaoEncontradoException exception = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> clienteAdapter.buscarCliente(cpf)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(customerClient, times(1)).buscarCliente(cpf);
    }

    @Test
    void deveLancarExcecaoQuandoCorpoDaRespostaEhNulo() {
        String cpf = "98765432109";
        ResponseEntity<Cliente> response = ResponseEntity.ok(null);

        when(customerClient.buscarCliente(cpf)).thenReturn(response);

        ClienteNaoEncontradoException exception = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> clienteAdapter.buscarCliente(cpf)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());
        verify(customerClient, times(1)).buscarCliente(cpf);
    }
}
