package br.org.isbet.clinica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.isbet.clinica.entities.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNomeContaining(String nome);
    List<Paciente> findAllByAtivoTrue();
    Paciente findByUsuarioUsername(String username);
}