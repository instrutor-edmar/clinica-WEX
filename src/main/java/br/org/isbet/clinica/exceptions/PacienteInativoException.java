package br.org.isbet.clinica.exceptions;

public class PacienteInativoException extends IllegalArgumentException {
    public PacienteInativoException(String message) {
        super(message);
    }
}
