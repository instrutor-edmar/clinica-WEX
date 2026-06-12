package br.org.isbet.clinica.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com token válido")
public record TokenDTO(String token) {
}