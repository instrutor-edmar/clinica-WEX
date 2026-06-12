package br.org.isbet.clinica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import br.org.isbet.clinica.entities.Paciente;
import br.org.isbet.clinica.entities.Usuario;
//import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNomeContaining(String nome);
    List<Paciente> findAllByAtivoTrue();
    /*Optional<Paciente>*/ Paciente findByUsuario(Usuario usuario);
    /*Optional<Paciente>*/Paciente findByUsuarioUsername(String username);
    Page<Paciente> findAllByAtivoTrue(Pageable pageable);
    @Query(value = "select * from pacientes where nome ilike %:nome%", nativeQuery = true)
    List<Paciente> findByNomeIlike(@Param("nome") String nome);
}