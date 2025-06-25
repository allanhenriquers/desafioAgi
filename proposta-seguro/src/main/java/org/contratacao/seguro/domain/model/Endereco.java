package org.contratacao.seguro.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    private String logradouro;

    private String numero;

    private String complemento;

    private String cidade;

    private String estado;

    private String cep;
}
