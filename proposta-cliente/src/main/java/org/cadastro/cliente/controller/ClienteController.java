package org.cadastro.cliente.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> criarCliente(@RequestBody @Valid Cliente cliente) {
        Cliente criado = clienteService.cadastrar(cliente);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{cpf}").buildAndExpand(criado.getCpf()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable String cpf) {
        return clienteService.buscarPorCpf(cpf).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable String cpf, @RequestBody @Valid Cliente cliente) {
        Cliente atualizado = clienteService.atualizar(cpf, cliente);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String cpf) {
        clienteService.remover(cpf);
        return ResponseEntity.noContent().build();
    }
}
