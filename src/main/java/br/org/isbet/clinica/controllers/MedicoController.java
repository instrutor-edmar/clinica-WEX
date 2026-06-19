package br.org.isbet.clinica.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.isbet.clinica.dtos.MedicoDTO;
import br.org.isbet.clinica.dtos.MedicoFormDTO;
import br.org.isbet.clinica.dtos.MedicoUpdateDTO;
import br.org.isbet.clinica.services.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/medicos")
@Tag(name = "Médicos", description = "Endpoints para gerenciar médicos")
public class MedicoController {
	private final MedicoService medicoService;
	
	public MedicoController(MedicoService medicoService) {
		this.medicoService = medicoService;
	}

	@Operation(summary = "Listar médicos", description = "Retorna todos os médicos disponíveis")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de médicos paginada (pode ser vazia)"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
	@GetMapping("/listar")
    public ResponseEntity<Page<MedicoDTO>> getAllMedicos(
            @ParameterObject
            @Parameter(description = "Parâmetros de paginação e ordenação")
            @PageableDefault(size = Integer.MAX_VALUE, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(this.medicoService.getAllMedicos(pageable, authentication));
	}

    @Operation(summary = "Cadastrar médico", description = "Cadastra um novo médico no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
	@ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso")
	@PostMapping("/register")
	@Transactional
	public ResponseEntity<MedicoDTO> createMedico(@RequestBody @Valid MedicoFormDTO medico) {
		return ResponseEntity.status(201).body(this.medicoService.createMedico(medico));
	}

    @Operation(summary = "Ativar médico", description = "Ativa um médico previamente cadastrado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
            @ApiResponse(responseCode = "400", description = "Médico já está ativo")
    })
    @PutMapping("/{id}/ativar")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<MedicoDTO> ativarMedico(@PathVariable Long id) {
        return ResponseEntity.ok(this.medicoService.ativarMedico(id));
    }
	
	@Operation(summary = "Buscar médico por nome", description = "Busca médicos pelo nome")
	@ApiResponses({
	        @ApiResponse(responseCode = "200", description = "Lista de médicos encontrados (pode ser vazia)"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
	@GetMapping("/buscarPorNome")
	public ResponseEntity<List<MedicoDTO>> getMedicoByNome(@RequestParam String nome) {
		 List<MedicoDTO> medicos = this.medicoService.getMedicoByNome(nome);
         if (medicos == null) {
             return ResponseEntity.ok(List.of());
         }
		 return ResponseEntity.ok(medicos);
	}

    @Operation(summary = "Buscar médico por id", description = "Busca médicos pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> getMedicoById(@PathVariable Long id) {
        MedicoDTO buscarMedico = this.medicoService.getMedicoById(id);
        if (buscarMedico == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(buscarMedico);
    }

	@Operation(summary = "Atualizar médico", description = "Atualiza os dados de um médico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do médico atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<MedicoDTO> atualizarMedico(@PathVariable Long id, @RequestBody @Valid MedicoUpdateDTO medico) {
		MedicoDTO medicoAtualizado = this.medicoService.atualizarMedico(id, medico);
        if (medicoAtualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicoAtualizado);
	}
	
	@Operation(summary = "Deletar médico", description = "Deleta um médico por ID")
	@ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dados do médico deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    @PatchMapping("/{id}")
	@Transactional
	public ResponseEntity<MedicoDTO> desativarMedico(@PathVariable Long id) {
		MedicoDTO medicoDeletado = this.medicoService.desativarMedico(id);
        if (medicoDeletado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medicoDeletado);
	}
}