package br.org.isbet.clinica.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Objeto utilizado para atualizar dados de uma consulta")
public record ConsultaUpdateDTO(
        @NotNull(message = "A nova data e horário são obrigatórios")
        @Future(message = "A nova data ou horário da consulta deve ser futura")
        @JsonFormat(
                pattern = "dd-MM-yyyy HH:mm",
                timezone = "America/Sao_Paulo"
        )
        @Schema(description = "Data e hora da consulta", example = "31-07-2026 14:00")
        LocalDateTime dataHora
) {
}