package org.cadastro.cliente.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.application.service.ClienteService;
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
    public ResponseEntity<ClienteResponseDTO> criarCliente(@RequestBody @Valid ClienteRequestDTO clienteRequest) {
        ClienteResponseDTO criado = clienteService.cadastrar(clienteRequest);
        URI location = getLocation(criado);
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDTO> buscarCliente(@PathVariable String cpf) {
        return clienteService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable String cpf, @RequestBody @Valid ClienteRequestDTO cliente) {
        ClienteResponseDTO atualizado = clienteService.atualizar(cpf, cliente);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletarCliente(@PathVariable String cpf) {
        clienteService.remover(cpf);
        return ResponseEntity.noContent().build();
    }

    private static URI getLocation(ClienteResponseDTO criado) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{cpf}")
                .buildAndExpand(criado.getCpf())
                .toUri();
    }
}
