package br.org.isbet.clinica.config;

import br.org.isbet.clinica.dtos.RoleDTO;
import br.org.isbet.clinica.entities.Role;
import br.org.isbet.clinica.entities.Usuario;
import br.org.isbet.clinica.repositories.RoleRepository;
import br.org.isbet.clinica.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    private final RoleRepository roleRepository;

    public AdminSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    @Order(2)
    public CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String emailAdmin = "admin@clinica.com";
            if (usuarioRepository.findByUsername(emailAdmin) == null) {
                Usuario admin = new Usuario();
                admin.setUsername(emailAdmin);
                admin.setPassword(passwordEncoder.encode("admin123"));
                Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN");
                if (roleAdmin == null) {
                    roleAdmin = new Role(new RoleDTO(null, "ROLE_ADMIN"));
                    roleAdmin = roleRepository.save(roleAdmin);
                }
                admin.adicionarRole(roleAdmin);
                usuarioRepository.save(admin);
            }
        };
    }
}
