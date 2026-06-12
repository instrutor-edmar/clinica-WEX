package br.org.isbet.clinica.services;

import br.org.isbet.clinica.entities.Consulta;
import br.org.isbet.clinica.entities.Paciente;
import br.org.isbet.clinica.entities.Status;
import br.org.isbet.clinica.exceptions.PacienteInativoException;
import br.org.isbet.clinica.repositories.ConsultaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Service
public class ConsultaValidador {

    private final ConsultaRepository consultaRepository;
    private final PermissaoService permissaoService;

    private static final LocalTime ABERTURA = LocalTime.of(7, 0);
    private static final LocalTime FECHAMENTO = LocalTime.of(19, 0);
    private static final Duration DURACAO = Duration.ofHours(1);
    private static final Duration ANTECEDENCIA_MINIMA = Duration.ofMinutes(30);
    private static final Duration ANTECEDENCIA_MINIMA_CANCELAMENTO = Duration.ofHours(24);

    public ConsultaValidador(ConsultaRepository consultaRepository, PermissaoService permissaoService) {
        this.consultaRepository = consultaRepository;
        this.permissaoService = permissaoService;
    }

    public void validarAgendamento(Paciente paciente, LocalDateTime dataHora, Long consultaIdAtual) {
        if (!Boolean.TRUE.equals(paciente.getAtivo())) {
            throw new PacienteInativoException("Paciente inativo");
        }
        if (!antecedenciaValida(dataHora, ANTECEDENCIA_MINIMA)) {
            throw new IllegalArgumentException("Agendamento deve ser feito com pelo menos 30 minutos de antecedência");
        }
        if (!horarioValido(dataHora)) {
            throw new IllegalArgumentException("Data/hora fora do horário de funcionamento da clínica");
        }
        if (temConsultaNoMesmoDia(paciente, dataHora.toLocalDate(), consultaIdAtual)) {
            throw new IllegalArgumentException("Paciente já possui consulta no mesmo dia");
        }
    }

    public void validarCancelamento(Consulta consulta, Authentication authentication) {
        if (consulta.getStatus() != Status.AGENDADA) {
            throw new IllegalArgumentException("Apenas consultas agendadas podem ser canceladas");
        }
        if (permissaoService.isPaciente(authentication) && !permissaoService.isAdmin(authentication) && !permissaoService.isMedico(authentication)) {
            if (!antecedenciaValida(consulta.getDataHora(), ANTECEDENCIA_MINIMA_CANCELAMENTO)) {
                throw new IllegalArgumentException("Cancelamento por pacientes deve ser feito com pelo menos 24 horas de antecedência");
            }
        }
    }

    private boolean antecedenciaValida(LocalDateTime dataHora, Duration antecedenciaMinima) {
        return Duration.between(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")), dataHora).compareTo(antecedenciaMinima) >= 0;
    }

    private boolean horarioValido(LocalDateTime dataHora) {
        DayOfWeek dia = dataHora.getDayOfWeek();
        if (dia == DayOfWeek.SUNDAY) return false;
        LocalTime hora = dataHora.toLocalTime();
        LocalTime fimConsulta = hora.plus(DURACAO);
        return !hora.isBefore(ABERTURA) && !fimConsulta.isAfter(FECHAMENTO);
    }

    private boolean temConsultaNoMesmoDia(Paciente paciente, LocalDate data, Long consultaIdAtual) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.atTime(23, 59, 59);
        return consultaRepository.findAllByPacienteAndDataHoraBetween(paciente, inicio, fim).stream()
                .anyMatch(c -> c.getStatus() != Status.CANCELADA && (consultaIdAtual == null || !c.getId().equals(consultaIdAtual)));
    }
}