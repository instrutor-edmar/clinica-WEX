package br.org.isbet.clinica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.org.isbet.clinica.entities.Medico;
import br.org.isbet.clinica.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    @Query(value = "select * from pacientes where nome ilike %:nome%", nativeQuery = true)
    List<Medico> findByNomeIlike(@Param("nome") String nome);
    List<Medico> findByNomeContaining(String nome);
    List<Medico> findAllByAtivoTrue();
    /*Optional<Medico>*/Medico findByUsuarioUsername(String username);
    /*Optional<Medico>*/ Medico findByUsuario(Usuario usuario);
    Page<Medico> findAllByAtivoTrue(Pageable pageable);
}
