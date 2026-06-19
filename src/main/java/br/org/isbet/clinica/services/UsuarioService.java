package br.org.isbet.clinica.services;

import br.org.isbet.clinica.repositories.MedicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.isbet.clinica.dtos.LoginDTO;
import br.org.isbet.clinica.dtos.UsuarioDTO;
import br.org.isbet.clinica.entities.Usuario;
import br.org.isbet.clinica.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, MedicoRepository medicoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO cadastrarUsuario(LoginDTO loginDTO) {
        var usuario = new Usuario(loginDTO);
        usuario.setPassword(passwordEncoder.encode(loginDTO.password()));
        usuarioRepository.save(usuario);

        return new UsuarioDTO(usuario);
    }

    public UsuarioDTO apagarUsuario(Long id) {
        var usuario = usuarioRepository.getReferenceById(id);
        usuarioRepository.deleteById(id);
        return new UsuarioDTO(usuario);
    }

    public Page<UsuarioDTO> getAllUsuarios(Pageable pageable) {
        return this.usuarioRepository.findAll(pageable).map(UsuarioDTO::new);
    }

}