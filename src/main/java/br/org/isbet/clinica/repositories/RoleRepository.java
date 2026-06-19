package br.org.isbet.clinica.repositories;

import br.org.isbet.clinica.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}
