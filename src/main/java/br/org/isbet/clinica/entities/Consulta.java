package br.org.isbet.clinica.entities;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    @JsonIgnoreProperties("Consultas")
    private Paciente paciente;

    @Column(name = "cpf_paciente", nullable = false)
    private String cpf;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false)
    @JsonIgnoreProperties("Consultas")
    private Medico medico;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private Status status;

    private CategoriaCancelamento motivoCancelamento;

    private String descricaoCancelamento;

    public Consulta(){
        super();
    }

    public Consulta(
        Paciente paciente, 
        Medico medico,
        LocalDateTime dataHora,
        String decricao){
            this.paciente=paciente;
            this.cpf=paciente.getCPF();
            this.medico=medico;
            this.status=Status.AGENDADA;
            this.motivoCancelamento=null;
            this.descricaoCancelamento=null;
        }
    
    public Long getId(){ return id;}
    public void setId(Long id){this.id=id;}
    public Paciente getPaciente(){ return paciente;}
    public void setPaciente(Paciente paciente){this.paciente=paciente;}
    public Medico getMedico(){ return medico;}
    public void setMedico(Medico medico){this.medico=medico;}
    public String getCpf(){ return cpf;}
    public void setCpf(String cpf){this.cpf=cpf;}
    public String getDescricao(){ return descricao;}
    public void setDescricao(String descricao){
        this.descricao=descricao;
    }
    public String getDescricaoCancelamento(){ 
        return descricaoCancelamento;
    }
    public void setDescricaoCancelamento(String descricaoCancelamento){
        this.descricaoCancelamento=descricaoCancelamento;
    }
    public CategoriaCancelamento getMotivoCancelamento(){ 
        return motivoCancelamento;
    }
    public void setMotivoCancelamento(CategoriaCancelamento motivoCancelamento){
        this.motivoCancelamento=motivoCancelamento;
    }
    public Status getStatus(){
        return status;
    }
    public void setStatus(Status status){
        this.status=status;
    }
    public LocalDateTime getDataHora(){
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora){
        this.dataHora=dataHora;
    }

}
