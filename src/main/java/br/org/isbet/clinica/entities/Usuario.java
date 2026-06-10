package br.org.isbet.clinica.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import br.org.isbet.clinica.dtos.LoginDTO;

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

    /*Lista de papéis que uma usuário pode ter*/
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios-roles",
        joinColumns = @JoinColumn(name = "idUsuario"),
        inverseJoinColumns = @JoinColumn(name = "idRoles")
    )
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

    public Usuario(LoginDTO usuarioDTO){
        this.username = usuarioDTO.username();
        this.password = usuarioDTO.password();
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword(){
        return password;
    }

    public List<Role> getRoles(){
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return roles;
    }

}
