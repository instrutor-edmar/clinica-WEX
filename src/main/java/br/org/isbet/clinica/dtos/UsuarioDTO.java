package br.org.isbet.clinica.dtos;

import br.org.isbet.clinica.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de busca de registro dados do usuário")
public record UsuarioDTO(Usuario usuario) {

}
