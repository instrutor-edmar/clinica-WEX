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
import org.springframework.web.bind.annotation.DeleteMapping;
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
@Tag(name = "Médicos", description = "Endpoint para gerenciar médicos")
public class MedicoController {  
    private MedicoService medicoService;
    
    public MedicoController(MedicoService medicoService){
        this.medicoService = medicoService;
    }

    @Operation(summary = "Cadastro de médicos", description = "Cadastrar novo médico")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Médico cadastrado!"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
        @ApiResponse(responseCode = "500", description = "Erro de Servidor!")
    })
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<MedicoDTO> cadastrarMedico(@RequestBody @Valid MedicoFormDTO medicoDTO){
        var medico = medicoService.cadastrarMedico(medicoDTO);
        return ResponseEntity.status(201).body(medico);
    }

    @Operation(summary = "Deletar de médico", description = "Deletar médico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médico deletado!"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
        @ApiResponse(responseCode = "500", description = "Erro de Servidor!")
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<MedicoDTO> deletarMedico(@PathVariable Long id){
        var medico = this.medicoService.desativarMedico(id);
        if(medico != null){
            return ResponseEntity.ok(medico);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Atualizar médico", description = "Deletar médico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Médico deletado!"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
        @ApiResponse(responseCode = "500", description = "Erro de Servidor!")
    })
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<MedicoDTO> atualizarMedico(
                                @PathVariable 
                                Long id, 
                                @RequestBody @Valid 
                                MedicoUpdateDTO medico){
        var dadosMedicoAtualizar = this.medicoService.atualizarMedico(id, medico);
        if(medico != null){
            return ResponseEntity.ok(dadosMedicoAtualizar);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Ativar médico", description = "Ativar médico")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/ativar")
    @Transactional
    public ResponseEntity<MedicoDTO> ativarMedico(@PathVariable Long id){
        return ResponseEntity.ok(this.medicoService.ativarMedico(id));
    }

    @Operation(summary = "Buscar médico por nome", description = "Buscar médico por nome")
    @GetMapping("/buscarPorNome")
    @Transactional
    public ResponseEntity <List<MedicoDTO>> buscarMedicoPorNome(@RequestParam String nome){
        List<MedicoDTO> medicos = this.medicoService.getMedicoByNome(nome);
        if(medicos == null){
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(medicos);
    }

    @Operation(summary = "Buscar médico por id", description = "Buscar médico por id")
    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity <MedicoDTO> buscarMedicoPorId(@PathVariable Long id){
        var medico = this.medicoService.getMedicoById(id);
        if(medico == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(medico);
    }

    @Operation(summary = "Listar Médicos", description = "Listar médicos")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuários listados!"),
        @ApiResponse(responseCode = "500", description = "Erro de Servidor!")
    })
    @GetMapping("/listar")
    @Transactional
    public ResponseEntity<Page<MedicoDTO>> listarMedico(
        @ParameterObject
        @Parameter(description = "Parametro de paginação")
        @PageableDefault(size = Integer.MAX_VALUE, sort = "nome", direction = Sort.Direction.ASC) 
        Pageable pagina,
        Authentication authentication
    ){
        return ResponseEntity.ok(this.medicoService.getAllMedicos(pagina, authentication));
    }
}
