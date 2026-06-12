package br.org.isbet.clinica.services;

import br.org.isbet.clinica.entities.Medico;
import br.org.isbet.clinica.entities.Paciente;
import br.org.isbet.clinica.repositories.MedicoRepository;
import br.org.isbet.clinica.repositories.PacienteRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
//import java.util.Optional;

import br.org.isbet.clinica.entities.Usuario;

@Service
public class JWTokenService {
    @Value("${jwt.secret}")
    private String secret;  
    private static final String ISSUER = "API da clínica";

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            List<String> roles = usuario.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            var jwtBuilder = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())
                    .withClaim("roles", roles)
                    .withExpiresAt(dataExpiracao());

            // Adiciona ID do Paciente se for paciente
            // if (roles.contains("ROLE_PACIENTE")) {
            //     /*Optional<Paciente>*/ Paciente paciente = pacienteRepository.findByUsuario(usuario);
            //     paciente.ifPresent(p -> jwtBuilder.withClaim("pacienteId", p.getId()));
            // }

            if (roles.contains("ROLE_PACIENTE")) {
            Paciente paciente = pacienteRepository.findByUsuario(usuario);
            if (paciente != null) {
                jwtBuilder.withClaim("pacienteId", paciente.getId());
            }
        }

            // Adiciona ID do Médico se for médico
                  // if (roles.contains("ROLE_MEDICO")) {
            //     Optional<Medico> medico = medicoRepository.findByUsuario(usuario);
            //     medico.ifPresent(p -> jwtBuilder.withClaim("medicoId", p.getId()));
            // }

            if (roles.contains("ROLE_MEDICO")) {
                Medico medico = medicoRepository.findByUsuarioUsername(usuario.getUsername());
                if (medico != null) {
                    jwtBuilder.withClaim("medicoId", medico.getId());
                }
            }

            return jwtBuilder.sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    private Instant dataExpiracao() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
            .plusHours(2)
            .toInstant();
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER) 
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            //throw new RuntimeException("Token JWT inválido ou expirado!");
              return null; 
        }
    }
}