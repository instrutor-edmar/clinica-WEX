package br.org.isbet.clinica.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de busca de registro roles do usuário")
public record RoleDTO(Long id, String role) {
}