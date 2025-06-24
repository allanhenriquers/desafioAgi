package org.contratacao.seguro.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Simulacao {
    private String clienteCpf;
    private TipoSeguro tipo;
    private BigDecimal valor;
}