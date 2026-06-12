package br.org.isbet.clinica.dtos;

import br.org.isbet.clinica.entities.Consulta;
import br.org.isbet.clinica.entities.Status;
import br.org.isbet.clinica.entities.CategoriaCancelamento;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Schema(description = "Objeto de resposta com os dados das consultas cadastradas")
public record ConsultaDTO(
        @Schema(description = "Identificador único do consulta", example = "1")
        Long id,

        @Schema(description = "Identificador único do paciente", example = "1")
        Long idPaciente,

        @Schema(description = "Nome do paciente", example = "João Silva")
        String nomePaciente,

        @Schema(description = "CPF do paciente", example = "12345678900")
        String cpfPaciente,

        @Schema(description = "Identificador único do médico", example = "1")
        Long idMedico,

        @Schema(description = "Nome do médico", example = "Dra. Ana")
        String nomeMedico,      
        
        @Schema(description = "Data e hora da consulta", example = "31-07-2026 14:00")
        @JsonFormat(
                pattern = "dd-MM-yyyy HH:mm",
                timezone = "America/Sao_Paulo"
        )
        LocalDateTime dataHora,

        @Schema(description = "Motivo da consulta", example = "Consulta de rotina")
        String descricao,

        @Schema(description = "Status da consulta", example = "AGENDADA, CANCELADA, CONCLUIDA")
        Status status,

        @Schema(description = "Motivo do cancelamento da consulta", example = "OUTROS, DESISTÊNCIA, CANCELAMENTO")
        CategoriaCancelamento motivoCancelamento,

        @Schema(description = "Motivo do cancelamento da consulta", example = "Indisposição")
        String descricaoCancelamento) {

    public ConsultaDTO(Consulta consulta) {
        this(consulta.getId(),
                consulta.getPaciente().getId(),
                consulta.getPaciente().getNome(),
                consulta.getCpf(), 
                consulta.getMedico().getId(),
                consulta.getMedico().getNome(),
                consulta.getDataHora(),
                consulta.getDescricao(),
                consulta.getStatus(),
                consulta.getMotivoCancelamento(),
                consulta.getDescricaoCancelamento()
        );
    }
}