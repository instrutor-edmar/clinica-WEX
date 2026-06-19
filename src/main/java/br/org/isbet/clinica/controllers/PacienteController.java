package br.org.isbet.clinica.controllers;

import br.org.isbet.clinica.dtos.PacienteDTO;
import br.org.isbet.clinica.dtos.PacienteFormDTO;
import br.org.isbet.clinica.dtos.PacienteUpdateDTO;
import br.org.isbet.clinica.services.ConsultaService;
import br.org.isbet.clinica.services.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para gerenciar pacientes")
public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService, ConsultaService consultaService) {
        this.pacienteService = pacienteService;
    }

    @Operation(summary = "Listar pacientes", description = "Retorna todos os pacientes disponíveis")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pacientes paginada"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/listar")
    public ResponseEntity<Page<PacienteDTO>> getAllPacientes(
            @ParameterObject
            @Parameter(description = "Parâmetros de paginação e ordenação")
            @PageableDefault(size = Integer.MAX_VALUE, sort = "nome", direction = Sort.Direction.ASC) 
            Pageable pageable) {
        return ResponseEntity.ok(this.pacienteService.getAllPacientes(pageable));

    }

    @Operation(summary = "Cadastrar paciente", description = "Cadastra um novo paciente no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso")
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<PacienteDTO> createPaciente(
            @Parameter(description = "Dados do paciente") 
            @RequestBody 
            @Valid PacienteFormDTO paciente) {
        return ResponseEntity.status(201).body(this.pacienteService.createPaciente(paciente));
    }

    @Operation(summary = "Buscar paciente por nome", description = "Busca pacientes pelo nome")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pacientes encontrados (pode ser vazia)"),
        @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @GetMapping("/buscarPorNome")
    public ResponseEntity<List<PacienteDTO>> getPacienteByNome(@RequestParam String nome) {
        List<PacienteDTO> pacientes = this.pacienteService.getPacienteByNome(nome);
        if (pacientes != null) {
            return ResponseEntity.ok(pacientes);
        } else {
            return ResponseEntity.ok(List.of());
        }
    }
    
    @Operation(summary = "Buscar paciente por id", description = "Busca pacientes pelo id")
    @GetMapping("/{id}")
    public ResponseEntity <PacienteDTO> getPacienteById(
            @Parameter(description = "ID do paciente", example = "1") 
            @PathVariable 
            Long id) {
        PacienteDTO buscarPaciente = this.pacienteService.getPacienteById(id);
        if (buscarPaciente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(buscarPaciente);
    }

    @Operation(summary = "Atualizar paciente", description = "Atualiza os dados de um paciente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PacienteDTO> atualizarPaciente(
            @Parameter(description = "ID do paciente", example = "1") 
            @PathVariable 
            Long id, 
            @Parameter(description = "Dados do paciente") 
            @RequestBody 
            @Valid 
            PacienteUpdateDTO paciente) {
        PacienteDTO pacienteAtualizado = this.pacienteService.atualizarPaciente(id, paciente);
        if (pacienteAtualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pacienteAtualizado);
    }

    @Operation(summary = "Deletar paciente", description = "Deleta um paciente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do paciente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<PacienteDTO> deletePaciente(
            @Parameter(description = "ID do paciente", example = "1") 
            @PathVariable 
            Long id) {
        PacienteDTO pacienteDeletado = this.pacienteService.desativarPaciente(id);
        if (pacienteDeletado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pacienteDeletado);
    }
}