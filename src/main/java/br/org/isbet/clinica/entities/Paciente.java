package br.org.isbet.clinica.entities;

import br.org.isbet.clinica.dtos.PacienteFormDTO;
import br.org.isbet.clinica.entities.Endereco;
import br.org.isbet.clinica.entities.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Entity;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Representação do paciente no sistema")
@Entity(name = "pacientes")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do paciente", example = "1")
    private Long id;
  
    @Schema(description = "Nome completo do paciente", example = "Mariana  Ribeiro")
    private String nome;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @Column(nullable = false, unique = true)
    @Schema(description = "Número do CPF do paciente", example = "12345678900")
    private String cpf;
    
    @Embedded
    private Endereco endereco;
    
    @Schema(description = "Telefone de contato", example = "(71) 99999-8888")
    private String telefone;
    
    @Column(nullable=false)
    private Boolean ativo;

    public Paciente(){
        super();
    }

    public Paciente (
        Long id, 
        Usuario usuario,
        String nome,
        String cpf,
        Endereco endereco,
        String telefone){
            this.id=id;
            this.usuario=usuario;
            this.nome=nome;
            this.cpf=cpf;
            this.endereco=endereco;
            this.telefone=telefone;
            this.ativo = true;
    }

    public Paciente(PacienteFormDTO pacienteForm) {
        super();
        this.nome = pacienteForm.nome();
        this.cpf = pacienteForm.cpf();
        this.endereco = pacienteForm.endereco() != null ? new Endereco(pacienteForm.endereco()) : null;
        this.telefone = pacienteForm.telefone();
        this.ativo = true;
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
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getCPF(){
        return cpf;
    }
    public String getUsername() {
        return usuario != null ? usuario.getUsername() : null;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
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
    public Boolean getAtivo(){
        return ativo;
    }
    public void setAtivo(Boolean status) { 
        this.ativo = status; 
    }

}
