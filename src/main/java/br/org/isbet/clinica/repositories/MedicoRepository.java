package br.org.isbet.clinica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.isbet.clinica.entities.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    List<Medico> findByNomeContaining(String nome);
    List<Medico> findAllByAtivoTrue();
    Medico findByUsuarioUsername(String username);
}
