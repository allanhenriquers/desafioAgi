package org.contratacao.seguro.client.model;

import lombok.Setter;

import java.time.LocalDate;

@Setter
public class Cliente {

    private String cpf;

    private String nome;

    private LocalDate dataNascimento;

    private String telefone;

    private Endereco endereco;
}