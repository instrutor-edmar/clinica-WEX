package br.org.isbet.clinica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.org.isbet.clinica.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
