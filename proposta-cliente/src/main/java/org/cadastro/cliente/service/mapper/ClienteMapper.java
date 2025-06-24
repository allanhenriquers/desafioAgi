package org.cadastro.cliente.service.mapper;

import org.cadastro.cliente.domain.model.Cliente;
import org.cadastro.cliente.domain.model.Endereco;
import org.cadastro.cliente.dto.ClienteRequestDTO;
import org.cadastro.cliente.dto.ClienteResponseDTO;
import org.cadastro.cliente.dto.EnderecoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponseDTO mapToResponse(Cliente cliente);

    Cliente mapToCliente(ClienteRequestDTO dto);

    Endereco mapEndereco(EnderecoDTO dto);

    EnderecoDTO mapEndereco(Endereco endereco);

}

