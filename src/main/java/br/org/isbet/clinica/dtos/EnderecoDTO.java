package br.org.isbet.clinica.dtos;

import br.org.isbet.clinica.entities.Endereco;
import jakarta.validation.constraints.NotBlank;

public record EnderecoDTO(
    @NotBlank(message = "logradouro não pode ser nulo")
    String logradouro,
       
    String numero,

    String complemento,

    @NotBlank(message = "bairro não pode ser nulo")
    String bairro,
    
    @NotBlank(message = "cidade não pode ser nulo")
    String cidade,

    @NotBlank(message = "estado não pode ser nulo")
    String estado,

    @NotBlank(message = "cep não pode ser nulo")
    String cep) {

    public EnderecoDTO(Endereco endereco){
        this(endereco.getLogradouro(),
             endereco.getNumero(),
             endereco.getComplemento(),
             endereco.getBairro(),
             endereco.getCidade(),
             endereco.getEstado(),
             endereco.getCep()
        );
    }
}
