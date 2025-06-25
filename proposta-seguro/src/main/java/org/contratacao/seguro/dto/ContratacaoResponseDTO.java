package org.contratacao.seguro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.contratacao.seguro.domain.model.TipoSeguro;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContratacaoResponseDTO {

    private String clienteCpf;

    private TipoSeguro tipo;

    private LocalDate dataContratacao;
}
