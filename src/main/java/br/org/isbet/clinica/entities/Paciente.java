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


public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cpf;
    
    private String nome;
    
    @Embedded
    private Endereco endereco;
    
    private String telefone;
    @Column(nullable=false)
    private Boolean ativo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

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
            this.ativo=true;
    }

    public String getNome(){
        return nome;
    }

    public String getCPF(){
        return cpf;
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

    public Boolean getAtivo(){
        return ativo;
    }

}
