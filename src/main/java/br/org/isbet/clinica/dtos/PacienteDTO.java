package br.org.isbet.clinica.dtos;

import br.org.isbet.clinica.entities.Endereco;
import br.org.isbet.clinica.entities.Paciente;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Objeto de resposta com os dados do paciente cadastrado")
public record PacienteDTO(
        @Schema(description = "Identificador único do paciente", example = "1")
        Long id,

        @Schema(description = "Nome completo do paciente", example = "Ana Maria Ribeiro")
        String nome,

        @Schema(description = "E-mail do paciente", example = "ana.maria@email.com")
        String username,

        @Schema(description = "Número do CPF do paciente", example = "12345678900")
        String cpf,

        @Schema(description = "Endereço do paciente")
        @NotNull(message = "O endereço não pode ser nulo")
        @Valid
        Endereco endereco,

        @Schema(description = "Telefone de contato", example = "(71) 99999-8777")
        String telefone) {

    public PacienteDTO(Paciente paciente) {
        this(paciente.getId(),
                paciente.getNome(),
                paciente.getUsername(),
                paciente.getCpf(),
                paciente.getEndereco(),
                paciente.getTelefone()
        );
    }
}