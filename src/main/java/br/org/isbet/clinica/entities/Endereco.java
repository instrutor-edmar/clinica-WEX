package br.org.isbet.clinica.entities;

import br.org.isbet.clinica.dtos.EnderecoDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;

@Embeddable
public class Endereco {
    @NotBlank(message = "logradouro não pode ser nulo")
    @Column(name = "logradouro")
    private String logradouro;
    
    @Column(name = "numero")
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @NotBlank(message = "bairro não pode ser nulo")
    @Column(name = "bairro")
    private String bairro;
    
    @NotBlank(message = "cidade não pode ser nulo")
    @Column(name = "cidade")
    private String cidade;

    @NotBlank(message = "estado não pode ser nulo")
    @Column(name = "estado")
    private String estado;

    @NotBlank(message = "cep não pode ser nulo")
    @Column(name = "cep")
    private String cep;

    public Endereco() {}

    public Endereco(EnderecoDTO endereco) {
        this.logradouro = endereco.logradouro();
        this.numero = endereco.numero();
        this.complemento = endereco.complemento();
        this.bairro = endereco.bairro();
        this.cidade = endereco.cidade();
        this.estado = endereco.estado();
        this.cep = endereco.cep();
    }

    public String getLogradouro(){
        return logradouro;
    }

    public void setLogradouro(String logradouro){
        this.logradouro = logradouro;
    }

    public String getNumero(){
        return numero;
    }

    public void setNumero(String numero){
        this.numero = numero;
    }

    public String getComplemento(){
        return complemento;
    }

    public void setComplemento(String complemento){
        this.complemento = complemento;
    }

    public String getBairro(){
        return bairro;
    }

    public void setBairro(String bairro){
        this.bairro = bairro;
    }

    public String getCidade(){
        return cidade;
    }

    public void setCidade(String cidade){
        this.cidade = cidade;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }
    
    public String getCep(){
        return cep;
    }

    public void setCep(String cep){
        this.cep = cep;
    }

}
