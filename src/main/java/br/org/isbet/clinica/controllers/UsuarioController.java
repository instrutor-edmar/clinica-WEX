package br.org.isbet.clinica.controllers;

import br.org.isbet.clinica.dtos.UsuarioDTO;
import br.org.isbet.clinica.dtos.LoginDTO;
import br.org.isbet.clinica.services.UsuarioService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UsuarioController {
    private Authentication manager;
    private UsuarioService usuarioservice;
    private JWTokenService tokenservice;
    

    // public UsuarioController(
    //     Authentication manager,
    //     UsuarioService usuarioservice,
    //     JWTokenService tokenservice){
            
    //         this.manager = manager;
    //         this.usuarioservice = usuarioservice;
    //         this.tokenservice = tokenservice;
    // }

    // @PostMapping("/register")
    // @Transactional
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(@RequestBody @Valid LoginDTO login){
        var usuario = usuarioservice.cadastrarUsuario(login);
        return ResponseEntity.status(201).body(usuario);
    }

}
