package br.org.isbet.clinica.dtos;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de resposta com os dados do login")
public record LoginDTO(
        @NotBlank(message = "O email de usuário é obrigatório.")
        String username,
        
        @NotBlank(message = "A senha é obrigatória.")
        String password) {
}