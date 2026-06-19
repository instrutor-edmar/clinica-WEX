package br.org.isbet.clinica.controllers;

import br.org.isbet.clinica.dtos.ConsultaCancelamentoDTO;
import br.org.isbet.clinica.dtos.ConsultaDTO;
import br.org.isbet.clinica.dtos.ConsultaFormDTO;
import br.org.isbet.clinica.dtos.ConsultaUpdateDTO;
import br.org.isbet.clinica.entities.Status;
import br.org.isbet.clinica.services.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping ("/consultas")
@Tag(name = "Consultas", description = "Endpoints para gerenciar consultas")
public class ConsultaController {
    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @Operation(summary = "Listar consultas", description = "Retorna todas as consultas disponíveis")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de consultas paginada"),
            @ApiResponse(responseCode = "204", description = "Dados vazios"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/listar")
    public Page<ConsultaDTO> getAllConsultas(
            @ParameterObject
            @Parameter(description = "Parâmetros de paginação e ordenação")
            @PageableDefault(size = Integer.MAX_VALUE, sort = "dataHora", direction = Sort.Direction.ASC) Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(this.consultaService.getAllConsultas(pageable, authentication)).getBody();
    }

    @Operation(summary = "Listar consultas por status", description = "Retorna as consultas por status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de consultas por status paginada"),
            @ApiResponse(responseCode = "204", description = "Dados vazios"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/listarPorStatus")
    public Page<ConsultaDTO> getConsultasByStatus(
            @Parameter(description = "Parâmetros de status", example = "AGENDADA")
            Status status,
            @ParameterObject
            @Parameter(description = "Parâmetros de paginação e ordenação")
            @PageableDefault(sort = "dataHora", direction = Sort.Direction.ASC) 
            Pageable pageable) {
        return ResponseEntity.ok(this.consultaService.getAllConsultasPorStatus(status, pageable)).getBody();
    }

    @Operation(summary = "Agendar consulta", description = "Cadastra uma nova consulta no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PostMapping("/agendar")
    @Transactional
    public ResponseEntity<ConsultaDTO> agendarConsulta(
            @Parameter(description = "Dados da consulta") 
            @RequestBody 
            @Valid 
            ConsultaFormDTO consulta,
            Authentication authentication) {
        ConsultaDTO novaConsulta = this.consultaService.agendar(consulta, authentication);
        return ResponseEntity.status(201).body(novaConsulta);
    }

    @Operation(summary = "Concluir consulta", description = "Altera o status da consulta para concluída")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta concluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ConsultaDTO> concluirConsulta(
            @Parameter(description = "ID da consulta", example = "1") 
            @PathVariable 
            Long id,
            Authentication authentication) {
        ConsultaDTO consultaConcluida = this.consultaService.concluirConsulta(id, authentication);
            return ResponseEntity.ok(consultaConcluida);
    }

    @Operation(summary = "Reagendar consulta", description = "Altera a data e hora de uma consulta agendada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta reagendada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou conflito de horário"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PutMapping("/reagendar/{id}")
    @Transactional
    public ResponseEntity<ConsultaDTO> reagendarConsulta(
            @Parameter(description = "ID da consulta", example = "1") 
            @PathVariable 
            Long id, 
            @Parameter(description = "Dados da consulta") 
            @RequestBody 
            @Valid 
            ConsultaUpdateDTO dados,
            Authentication authentication) {
        ConsultaDTO consultaReagendada = this.consultaService.alterarDataHoraConsulta(id, dados, authentication);
            return ResponseEntity.ok(consultaReagendada);
    }

    @Operation(summary = "Cancelar consulta", description = "Cancela consulta de um paciente agendada com um médico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<ConsultaDTO> cancelarConsulta(
         @Parameter(description = "ID da consulta", example = "1") 
         @PathVariable 
         Long id, 
         @Parameter(description = "motivo do cancelamento", example = "Indisponibilidade")
         @RequestBody 
         @Valid 
         ConsultaCancelamentoDTO dados,
         Authentication authentication) {
        ConsultaDTO consultaCancelada = this.consultaService.cancelarConsulta(id, dados, authentication);
        return ResponseEntity.ok(consultaCancelada);
    }

    @Operation(summary = "Listar horários disponíveis", description = "Retorna os horários disponíveis para um médico em uma data específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de horários disponíveis"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/disponibilidade")
    public ResponseEntity<List<LocalTime>> getHorariosDisponiveis(
            @Parameter(description = "ID do médico", example = "1") 
            @RequestParam 
            Long medicoId,
            @Parameter(description = "Data da consulta", example = "31-07-2026") 
            @RequestParam 
            LocalDate data) {
        return ResponseEntity.ok(this.consultaService.getHorariosDisponiveis(medicoId, data));
    }
}