package org.cadastro.cliente.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClienteResponseDTO {
    private String cpf;

    private String nome;

    private LocalDate dataNascimento;

    private String telefone;

    private EnderecoDTO endereco;
}
