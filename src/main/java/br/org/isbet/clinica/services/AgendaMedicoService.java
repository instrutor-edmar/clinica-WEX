package br.org.isbet.clinica.services;

import br.org.isbet.clinica.entities.Consulta;
import br.org.isbet.clinica.entities.Medico;
import br.org.isbet.clinica.entities.Status;
import br.org.isbet.clinica.exceptions.MedicoIndisponivelException;
import br.org.isbet.clinica.repositories.ConsultaRepository;
import br.org.isbet.clinica.repositories.MedicoRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AgendaMedicoService {

    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;

    private static final LocalTime ABERTURA = LocalTime.of(7, 0);
    private static final LocalTime FECHAMENTO = LocalTime.of(19, 0);
    private static final Duration DURACAO = Duration.ofHours(1);
    private static final Duration ANTECEDENCIA_MINIMA = Duration.ofMinutes(30);

    public AgendaMedicoService(MedicoRepository medicoRepository, ConsultaRepository consultaRepository) {
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
    }

    public Medico buscarMedicoDisponivel(Long medicoId, LocalDateTime dataHora, Long consultaIdAtual) {
        if (medicoId != null) {
            Medico medico = medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));
            validarMedico(medico, dataHora, consultaIdAtual);
            return medico;
        } else {
            return medicoAleatorio(dataHora, consultaIdAtual)
                    .orElseThrow(() -> new IllegalArgumentException("Nenhum médico disponível na data/hora informada"));
        }
    }

    public void validarMedico(Medico medico, LocalDateTime dataHora, Long consultaIdAtual) {
        if (!Boolean.TRUE.equals(medico.getAtivo())) {
            throw new MedicoIndisponivelException("Médico inativo");
        }
        if (medicoOcupado(medico, dataHora, consultaIdAtual)) {
            throw new MedicoIndisponivelException("Médico já possui consulta nesse horário");
        }
    }

    public List<LocalTime> getHorariosDisponiveis(Long medicoId, LocalDate data) {
        if (data.getDayOfWeek() == DayOfWeek.SUNDAY) return List.of();

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

        List<LocalTime> horarios = new ArrayList<>();
        LocalTime horarioAtual = ABERTURA;
        while (horarioAtual.plus(DURACAO).isBefore(FECHAMENTO) || horarioAtual.plus(DURACAO).equals(FECHAMENTO)) {
            horarios.add(horarioAtual);
            horarioAtual = horarioAtual.plus(DURACAO);
        }

        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.atTime(23, 59, 59);
        List<Consulta> consultasAgendadas = consultaRepository.findAllByMedicoAndDataHoraBetween(medico, inicioDia, fimDia);

        horarios.removeIf(horario -> consultasAgendadas.stream()
                .filter(c -> c.getStatus() != Status.CANCELADA)
                .anyMatch(c -> Math.abs(Duration.between(c.getDataHora().toLocalTime(), horario).toMinutes()) < DURACAO.toMinutes()));

        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));
        if (data.equals(hoje)) {
            LocalTime agora = LocalTime.now(ZoneId.of("America/Sao_Paulo"));
            horarios.removeIf(h -> Duration.between(agora, h).compareTo(ANTECEDENCIA_MINIMA) < 0);
        }
        return horarios;
    }

    private boolean medicoOcupado(Medico medico, LocalDateTime dataHora, Long consultaIdAtual) {
        LocalDateTime inicio = dataHora.toLocalDate().atStartOfDay();
        LocalDateTime fim = dataHora.toLocalDate().atTime(23, 59, 59);
        return consultaRepository.findAllByMedicoAndDataHoraBetween(medico, inicio, fim).stream()
                .anyMatch(c -> c.getStatus() != Status.CANCELADA && (consultaIdAtual == null || !c.getId().equals(consultaIdAtual)) && Math.abs(Duration.between(c.getDataHora(), dataHora).toMinutes()) < DURACAO.toMinutes());
    }

    private Optional<Medico> medicoAleatorio(LocalDateTime dataHora, Long consultaIdAtual) {
        List<Medico> candidatos = new ArrayList<>(medicoRepository.findAllByAtivoTrue());
        candidatos.removeIf(m -> medicoOcupado(m, dataHora, consultaIdAtual));
        if (candidatos.isEmpty()) return Optional.empty();
        return Optional.of(candidatos.get(ThreadLocalRandom.current().nextInt(candidatos.size())));
    }
}