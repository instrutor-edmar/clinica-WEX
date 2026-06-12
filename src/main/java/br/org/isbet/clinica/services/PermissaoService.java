package br.org.isbet.clinica.services;

import br.org.isbet.clinica.entities.Paciente;
import br.org.isbet.clinica.repositories.PacienteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class PermissaoService {

    private final PacienteRepository pacienteRepository;

    public PermissaoService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente buscarPacienteAutorizado(Long pacienteId, Authentication authentication) {
        String currentUsername = authentication.getName();
        Paciente paciente;

        if (pacienteId != null) {
            paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        } else {
            paciente = pacienteRepository.findByUsuarioUsername(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado para o usuário logado."));
        }

        validarPermissaoPaciente(authentication, paciente);
        return paciente;
    }

    public void validarPermissaoPaciente(Authentication authentication, Paciente paciente) {
        String currentUsername = authentication.getName();
        boolean usuarioPrivilegiado = isAdmin(authentication) || isMedico(authentication);
        boolean ehDonoDaConsulta = paciente.getUsername().equals(currentUsername);

        if (!usuarioPrivilegiado && !ehDonoDaConsulta) {
            throw new AccessDeniedException("Você não tem permissão para realizar esta ação.");
        }
    }

    public boolean isAdmin(Authentication auth) { return possuiRole(auth, "ADMIN"); }
    public boolean isMedico(Authentication auth) { return possuiRole(auth, "MEDICO"); }
    public boolean isPaciente(Authentication auth) { return possuiRole(auth, "PACIENTE"); }

    private boolean possuiRole(Authentication authentication, String role) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role) || a.getAuthority().equals("ROLE_" + role));
    }
}