package br.org.isbet.clinica.entities;

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


public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String crm;
    
    private String nome;
    
    @Embedded
    private Endereco endereco;
    
    private String telefone;
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    private EspecialidadeMedico especialidade;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    public Medico(){
        super();
    }

    public Medico (
        Long id, 
        Usuario usuario,
        String nome,
        String crm,
        Endereco endereco,
        String telefone,
        EspecialidadeMedico especialidade){
            this.id=id;
            this.usuario=usuario;
            this.nome=nome;
            this.crm=crm;
            this.endereco=endereco;
            this.telefone=telefone;
            this.especialidade=especialidade;
            this.ativo=false;
    }

    public void validarMedico(){
        this.ativo=true;
    }

    public String getNome(){
        return nome;
    }

    public String getCRM(){
        return crm;
    }

    public Long getId(){
        return id;
    }

     public Usuario getUsuario(){
        return usuario;
    }

    public String getEmail(){
        return usuario !=null ? usuario.getUsername(): null;
    }

    public String getTelefone(){
        return telefone;
    }

    public Endereco getEndereco(){
        return endereco;
    }

    public EspecialidadeMedico getEspecialidade(){
        return especialidade;
    }

    public Boolean getAtivo(){
        return ativo;
    }

}
