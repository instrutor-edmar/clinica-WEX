package br.org.isbet.clinica.exceptions;

public class MedicoIndisponivelException extends IllegalArgumentException {
    public MedicoIndisponivelException(String message) {
        super(message);
    }
}