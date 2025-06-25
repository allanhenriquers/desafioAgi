package org.contratacao.seguro.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {

    private String cpf;

    private String nome;

    private LocalDate dataNascimento;

    private String telefone;

    private Endereco endereco;
}
