package br.org.isbet.clinica.entities;

import br.org.isbet.clinica.dtos.MedicoFormDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Schema(description = "Representação do paciente no sistema")
@Entity(name = "medicos")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do médico", example = "1")
    private Long id;
    
    @Schema(description = "Nome completo do médico", example = "Dra. Ana Paula Ribeiro")
    private String nome;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @Column(nullable = false, unique = true)
    @Schema(description = "Número do CRM do médico", example = "123456")
    private String crm;

    @Embedded
    private Endereco endereco;
    
    @Enumerated(EnumType.STRING)
    @Schema(description = "Especialidade do médico", example = "CARDIOLOGIA")
    private EspecialidadeMedico especialidade;
    
    @Schema(description = "Telefone de contato", example = "(71) 99999-8888")
    private String telefone;
           
    private Boolean ativo;

    public Medico(){
        super();
    }

	public Medico(MedicoFormDTO medicoForm) {
		super();
		this.nome = medicoForm.nome();
		this.crm = medicoForm.crm();
		this.endereco = medicoForm.endereco() != null ? new Endereco(medicoForm.endereco()) : null;
        this.telefone = medicoForm.telefone();
        this.especialidade = medicoForm.especialidade();
        this.ativo = false;
	}

    public void validarMedico(){
        this.ativo=true;
    }
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    public String getNome(){
        return nome;
    }
    public void setNome(String nome) {
		this.nome = nome;
	}
    public Usuario getUsuario(){
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getEmail(){
        return usuario !=null ? usuario.getUsername(): null;
    }
    public String getCRM(){
        return crm;
    }
    public void setCrm(String crm) {
		this.crm = crm;
	}
    public String getTelefone(){
        return telefone;
    }
    public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
    public Endereco getEndereco(){
        return endereco;
    }
    public void setEndereco(Endereco endereco) { 
        this.endereco = endereco; 
    }
    public EspecialidadeMedico getEspecialidade(){
        return especialidade;
    }
    public void setEspecialidade(EspecialidadeMedico especialidade) {
		this.especialidade = especialidade;
	}
    public Boolean getAtivo(){
        return ativo;
    }
    public void setAtivo(Boolean status) { 
        this.ativo = status; 
    }
}
