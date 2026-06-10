package br.org.isbet.clinica.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import br.org.isbet.clinica.entities.Usuario;

@Schema(description = "Objeto de busca de registros de usuários")
public record UsuarioDTO(Usuario usuario) {

}
