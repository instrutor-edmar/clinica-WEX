package br.org.isbet.clinica.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.isbet.clinica.entities.Usuario;
import br.org.isbet.clinica.dtos.LoginDTO;
import br.org.isbet.clinica.dtos.UsuarioDTO;
import br.org.isbet.clinica.repositories.UsuarioRepository;

@Service
public class UsuarioService {
    private UsuarioRepository usuarioRepository;
    //private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
            this.usuarioRepository = usuarioRepository;
        // this.passwordEncoder = passwordEncoder;
    } 

    public UsuarioDTO cadastrarUsuario(LoginDTO login){
        var usuario = new Usuario(login);
        //usuario.setPassWord(passwordEncoder.encode(login.password()));
        usuarioRepository.save(usuario);
        
        //verificar a assinatura do usuarioDTO
        return new UsuarioDTO(usuario);
    }
}
