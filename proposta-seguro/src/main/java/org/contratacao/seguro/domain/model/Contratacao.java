package org.contratacao.seguro.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "contratacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contratacao {

    @Id
    private UUID id;

    @Column(name = "cliente_cpf", length = 11, nullable = false)
    private String clienteCpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSeguro tipo;

    @Column(name = "data_contratacao", nullable = false)
    private LocalDate dataContratacao;
}
