package br.org.isbet.clinica.services;

import br.org.isbet.clinica.dtos.ConsultaDTO;
import br.org.isbet.clinica.dtos.ConsultaFormDTO;
import br.org.isbet.clinica.dtos.ConsultaUpdateDTO;
import br.org.isbet.clinica.dtos.ConsultaCancelamentoDTO;
import br.org.isbet.clinica.entities.*;
import br.org.isbet.clinica.repositories.ConsultaRepository;
import br.org.isbet.clinica.exceptions.ConsultaNaoEncontradaException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final AgendaMedicoService agendaMedicoService;
    private final ConsultaValidador consultaValidador;
    private final PermissaoService permissaoService;

    public ConsultaService(
            ConsultaRepository consultaRepository,
            AgendaMedicoService agendaMedicoService,
            ConsultaValidador consultaValidador,
            PermissaoService permissaoService) {

        this.consultaRepository = consultaRepository;
        this.agendaMedicoService = agendaMedicoService;
        this.consultaValidador = consultaValidador;
        this.permissaoService = permissaoService;
    }

    @Transactional
    public ConsultaDTO agendar(ConsultaFormDTO consultaFormDTO, Authentication authentication) {
        Paciente paciente = permissaoService.buscarPacienteAutorizado(consultaFormDTO.idPaciente(), authentication);
        consultaValidador.validarAgendamento(paciente, consultaFormDTO.dataHora(), null);
        Medico medico = agendaMedicoService.buscarMedicoDisponivel(consultaFormDTO.idMedico(), consultaFormDTO.dataHora(), null);
        
        Consulta consulta = new Consulta(paciente, medico, consultaFormDTO.dataHora(), consultaFormDTO.descricao());
        
        return new ConsultaDTO(consultaRepository.save(consulta));
    }

    @Transactional
    public ConsultaDTO alterarDataHoraConsulta(Long id, ConsultaUpdateDTO consultaUpdateDTO, Authentication authentication) {
        Consulta consulta = buscarConsulta(id);
        LocalDateTime novaDataHora = consultaUpdateDTO.dataHora();

        permissaoService.buscarPacienteAutorizado(consulta.getPaciente().getId(), authentication);

        if (consulta.getStatus() != Status.AGENDADA) {
            throw new IllegalArgumentException("Apenas consultas agendadas podem ser reagendadas");
        }

        consultaValidador.validarAgendamento(consulta.getPaciente(), novaDataHora, consulta.getId());
        agendaMedicoService.validarMedico(consulta.getMedico(), novaDataHora, consulta.getId());

        consulta.setDataHora(novaDataHora);
        return new ConsultaDTO(consultaRepository.save(consulta));
    }

    @Transactional
    public ConsultaDTO concluirConsulta(Long consultaId, Authentication authentication) {
        Consulta consulta = buscarConsulta(consultaId);

        if (!permissaoService.isAdmin(authentication) && !permissaoService.isMedico(authentication)) {
            throw new AccessDeniedException("Apenas médicos ou administradores podem concluir uma consulta.");
        }

        if (consulta.getStatus() != Status.AGENDADA) {
            throw new IllegalArgumentException("Apenas consultas agendadas podem ser concluídas");
        }

        consulta.setStatus(Status.CONCLUÍDA);
        return new ConsultaDTO(consultaRepository.save(consulta));
    }
    
    @Transactional
    public ConsultaDTO cancelarConsulta(Long consultaId, ConsultaCancelamentoDTO cancelamentoDTO, Authentication authentication) {
        Consulta consulta = buscarConsulta(consultaId);
        
        permissaoService.buscarPacienteAutorizado(consulta.getPaciente().getId(), authentication);
        consultaValidador.validarCancelamento(consulta, authentication);
        
        consulta.setMotivoCancelamento(cancelamentoDTO.motivoCancelamento());
        consulta.setDescricaoCancelamento(cancelamentoDTO.descricaoCancelamento());
        consulta.setStatus(Status.CANCELADA);
        
        return new ConsultaDTO(consultaRepository.save(consulta));
    }

    public Page<ConsultaDTO> getAllConsultas(Pageable pageable, Authentication authentication) {
        String username = authentication.getName();

        if (permissaoService.isAdmin(authentication)) {
            return consultaRepository.findAll(pageable).map(ConsultaDTO::new);
        } else if (permissaoService.isMedico(authentication)) {
            return consultaRepository.findAllByMedicoUsuarioUsername(username, pageable).map(ConsultaDTO::new);
        } else if (permissaoService.isPaciente(authentication)) {
            return consultaRepository.findAllByPacienteUsuarioUsername(username, pageable).map(ConsultaDTO::new);
        } else {
            return Page.empty(pageable);
        }
    }

    public Page<ConsultaDTO> getAllConsultasPorStatus(Status status, Pageable pageable) {
        return consultaRepository.findAllByStatus(status, pageable).map(ConsultaDTO::new);
    }

    public List<LocalTime> getHorariosDisponiveis(Long medicoId, LocalDate data) {
        return agendaMedicoService.getHorariosDisponiveis(medicoId, data);
    }

    private Consulta buscarConsulta(Long id){
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ConsultaNaoEncontradaException("Consulta não encontrada"));
    }
}