package br.org.isbet.clinica.dtos;

import br.org.isbet.clinica.entities.CategoriaCancelamento;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto utilizado para cancelar uma consulta")
public record ConsultaCancelamentoDTO(
        @NotNull(message = "A categoria do cancelamento é obrigatória")
        CategoriaCancelamento motivoCancelamento,
        String descricaoCancelamento
        ) {

}