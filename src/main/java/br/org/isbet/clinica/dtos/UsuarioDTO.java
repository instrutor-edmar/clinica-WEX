package br.org.isbet.clinica.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de busca de registros de usuários")
public record UsuarioDTO(String username, String password) {

}
