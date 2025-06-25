package org.contratacao.seguro.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Simulacao {
    private String clienteCpf;
    private TipoSeguro tipo;
    private BigDecimal valor;
}