package org.cadastro.cliente.infra;

import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteRepositoryAdapterTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteRepositoryAdapter clienteRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarTrueQuandoCpfExistir() {
        String cpf = "12345678901";

        when(clienteRepository.existsById(cpf)).thenReturn(true);

        boolean existe = clienteRepositoryAdapter.existsByCpf(cpf);

        assertTrue(existe);
        verify(clienteRepository, times(1)).existsById(cpf);
    }

    @Test
    void deveRetornarFalseQuandoCpfNaoExistir() {
        String cpf = "00000000000";

        when(clienteRepository.existsById(cpf)).thenReturn(false);

        boolean existe = clienteRepositoryAdapter.existsByCpf(cpf);

        assertFalse(existe);
        verify(clienteRepository, times(1)).existsById(cpf);
    }

    @Test
    void deveSalvarClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setCpf("12345678901");
        cliente.setNome("Fulano");

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente salvo = clienteRepositoryAdapter.save(cliente);

        assertNotNull(salvo);
        assertEquals(cliente, salvo);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void deveEncontrarClientePorCpfQuandoExistir() {
        String cpf = "12345678901";
        Cliente cliente = new Cliente();
        cliente.setCpf(cpf);

        when(clienteRepository.findById(cpf)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteRepositoryAdapter.findByCpf(cpf);

        assertTrue(resultado.isPresent());
        assertEquals(cliente, resultado.get());
        verify(clienteRepository, times(1)).findById(cpf);
    }

    @Test
    void deveRetornarOptionalVazioQuandoClienteNaoExistir() {
        String cpf = "00000000000";

        when(clienteRepository.findById(cpf)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteRepositoryAdapter.findByCpf(cpf);

        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).findById(cpf);
    }

    @Test
    void deveDeletarClientePorCpf() {
        String cpf = "12345678901";

        doNothing().when(clienteRepository).deleteById(cpf);

        clienteRepositoryAdapter.deleteByCpf(cpf);

        verify(clienteRepository, times(1)).deleteById(cpf);
    }
}
