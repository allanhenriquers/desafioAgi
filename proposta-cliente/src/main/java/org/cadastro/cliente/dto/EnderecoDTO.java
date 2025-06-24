package org.cadastro.cliente.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class EnderecoDTO {

    private String logradouro;

    private String numero;

    private String complemento;

    private String cidade;

    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve conter 2 letras maiúsculas")
    private String estado;

    private String cep;
}
