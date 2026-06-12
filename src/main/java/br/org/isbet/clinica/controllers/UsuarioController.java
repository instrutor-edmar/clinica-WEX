package br.org.isbet.clinica.controllers;

import br.org.isbet.clinica.dtos.UsuarioDTO;
import br.org.isbet.clinica.entities.Usuario;
import br.org.isbet.clinica.services.JWTokenService;
import br.org.isbet.clinica.dtos.LoginDTO;
import br.org.isbet.clinica.dtos.TokenDTO;
import br.org.isbet.clinica.services.UsuarioService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


import org.springdoc.core.annotations.ParameterObject;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UsuarioController {
    private AuthenticationManager manager;
    private UsuarioService usuarioservice;
    private JWTokenService tokenservice;
    

    public UsuarioController(
        AuthenticationManager manager,
        UsuarioService usuarioservice,
        JWTokenService tokenservice){
            
            this.manager = manager;
            this.usuarioservice = usuarioservice;
            this.tokenservice = tokenservice;
    }

    @Operation(summary = "Login de usuário", description = "Logar usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário logado!"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
        @ApiResponse(responseCode = "500", description = "Erro de Servidor!")
    })
    @PostMapping("/login")
    @Transactional
    public ResponseEntity<TokenDTO> efetuarLogin(@RequestBody @Valid LoginDTO login){
        var dto = new UsernamePasswordAuthenticationToken(login.username(), login.password());
        var authentication = manager.authenticate(dto);
        var tokenJWT = tokenservice.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenDTO(tokenJWT));
    }

    @Operation(summary = "Cadastro de usuário", description = "Cadastrar novo usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário cadastrado!"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
        @ApiResponse(responseCode = "500", description = "Erro de Servidor!")
    })
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody @Valid LoginDTO login){
        var usuario = usuarioservice.cadastrarUsuario(login);
        return ResponseEntity.status(201).body(usuario);
    }

    @Operation(summary = "Deletar de usuário", description = "Deletar usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário deletado!"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos!"),
        @ApiResponse(responseCode = "500", description = "Erro de Servidor!")
    })
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<UsuarioDTO> deletarUsuario(@PathVariable Long id){
        var usuario = usuarioservice.deletarUsuario(id);
        if(usuario != null){
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Listar usuários", description = "Listar usuários")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuários listados!"),
        @ApiResponse(responseCode = "500", description = "Erro de Servidor!")
    })
    
    @GetMapping("/listar")
    @Transactional
    public ResponseEntity<Page <UsuarioDTO>> listarUsuario(
        @ParameterObject
        @Parameter(description = "Parametro de paginação")
        @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) 
        Pageable pagina
    ){
        return ResponseEntity.ok(this.usuarioservice.getAllUsuarios(pagina));
    }
}
