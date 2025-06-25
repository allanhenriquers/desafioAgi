package org.contratacao.seguro.infra.adapter;

import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.domain.model.repository.SeguroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SeguroRepositoryAdapterTest {

    @Mock
    private SeguroRepository seguroRepository;

    @InjectMocks
    private SeguroRepositoryAdapter seguroRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarContratacaoComSucesso() {
        Contratacao contratacao = new Contratacao();
        contratacao.setId(java.util.UUID.randomUUID());
        contratacao.setClienteCpf("12345678901");
        contratacao.setTipo(null); // ajuste conforme necessário
        contratacao.setDataContratacao(java.time.LocalDate.now());

        when(seguroRepository.save(contratacao)).thenReturn(contratacao);

        Contratacao resultado = seguroRepositoryAdapter.save(contratacao);

        assertNotNull(resultado);
        assertEquals(contratacao, resultado);
        verify(seguroRepository, times(1)).save(contratacao);
    }

    @Test
    void deveLancarExcecaoQuandoSalvarFalhar() {
        Contratacao contratacao = new Contratacao();
        when(seguroRepository.save(contratacao)).thenThrow(new RuntimeException("Erro ao salvar"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            seguroRepositoryAdapter.save(contratacao);
        });

        assertEquals("Erro ao salvar", exception.getMessage());
        verify(seguroRepository, times(1)).save(contratacao);
    }
}
