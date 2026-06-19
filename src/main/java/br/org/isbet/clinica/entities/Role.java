package br.org.isbet.clinica.entities;

import org.springframework.security.core.GrantedAuthority;
import br.org.isbet.clinica.dtos.RoleDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;

    public Role() {
        super();
    }

    public Role(RoleDTO roleDto) {
        this.role = roleDto.role();
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String getAuthority() {
        return role;
    }
}
