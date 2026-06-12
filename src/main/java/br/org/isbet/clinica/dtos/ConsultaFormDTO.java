package br.org.isbet.clinica.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Objeto utilizado para cadastrar dados de uma consulta")
public record ConsultaFormDTO(
        @Schema(description = "ID do paciente associado à consulta (Obrigatório apenas para ADMIN)", example = "1")
        Long idPaciente,

        @Schema(description = "ID do médico associado à consulta", example = "1")
        Long idMedico,

        @Schema(description = "Data e hora da consulta", example = "31-07-2026 14:00")
        @NotNull(message = "A data e horário não podem ser nulos")
        @Future(message = "A data da consulta deve ser futura")
        @JsonFormat(
                pattern = "dd-MM-yyyy HH:mm",
                timezone = "America/Sao_Paulo"
        )
        LocalDateTime dataHora,

        @Schema(description = "Descrição da consulta", example = "Consulta de rotina")
        @NotNull(message = "A descrição é obrigatória")
        String descricao){
}