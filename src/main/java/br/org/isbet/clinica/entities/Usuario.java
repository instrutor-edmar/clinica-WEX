package br.org.isbet.clinica.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import br.org.isbet.clinica.dtos.UsuarioDTO;

@Entity(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Schema(description = "E-mail de usuário", example = "usuario@email.com")
    private String username;

    @Schema(description = "Senha do usuário")
    private String password;

    private List<Role> roles = new ArrayList<>();

    public Usuario(){
        super();
    }

    public Usuario(Long id, String username, String password){
        super();
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Usuario(UsuarioDTO usuarioDTO){
        this.username = usuarioDTO.username();
        this.password = usuarioDTO.password();
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public List<Role> getAuthorities(){
        return roles;
    }


}
