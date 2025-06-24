package org.contratacao.seguro.client.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Endereco {

    private String logradouro;

    private String numero;

    private String complemento;

    private String cidade;

    private String estado;

    private String cep;
}