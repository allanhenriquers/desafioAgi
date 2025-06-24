package org.contratacao.seguro.controller;

import lombok.RequiredArgsConstructor;
import org.contratacao.seguro.domain.model.Contratacao;
import org.contratacao.seguro.domain.model.Simulacao;
import org.contratacao.seguro.domain.model.TipoSeguro;
import org.contratacao.seguro.service.SeguroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seguros")
@RequiredArgsConstructor
public class SeguroController {

    private final SeguroService seguroService;

    @PostMapping("/simular")
    public ResponseEntity<Simulacao> simularSeguro(@RequestParam String cpf, @RequestParam TipoSeguro tipo) {
        Simulacao simulacao = seguroService.simularSeguro(cpf, tipo);
        return ResponseEntity.ok(simulacao);
    }

    @PostMapping("/contratar")
    public ResponseEntity<Contratacao> contratarSeguro(@RequestParam String cpf, @RequestParam TipoSeguro tipo) {
        Contratacao contratacao = seguroService.contratarSeguro(cpf, tipo);
        return ResponseEntity.status(HttpStatus.CREATED).body(contratacao);
    }
}
