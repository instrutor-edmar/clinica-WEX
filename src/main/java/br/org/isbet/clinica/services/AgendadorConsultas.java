package br.org.isbet.clinica.services;

import br.org.isbet.clinica.entities.Consulta;
import br.org.isbet.clinica.entities.Status;
import br.org.isbet.clinica.repositories.ConsultaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class AgendadorConsultas {

    private final ConsultaRepository consultaRepository;

    public AgendadorConsultas(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Scheduled(fixedRate = 300000) // Executa a cada 5 minutos (300.000 ms)
    @Transactional
    public void finalizarConsultasExpiradas() {
        // Padronizando o timezone para evitar bugs dependendo da máquina servidora
        LocalDateTime limite = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).minusHours(1);

        List<Consulta> consultasExpiradas = consultaRepository.findAllByStatusAndDataHoraBefore(Status.AGENDADA, limite);

        if (!consultasExpiradas.isEmpty()) {
            for (Consulta consulta : consultasExpiradas) {
                consulta.setStatus(Status.CONCLUÍDA);
            }
            consultaRepository.saveAll(consultasExpiradas);
            System.out.println("Agendador: " + consultasExpiradas.size() + " consultas foram finalizadas automaticamente.");
        }
    }
}