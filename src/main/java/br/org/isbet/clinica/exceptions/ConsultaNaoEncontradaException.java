package br.org.isbet.clinica.exceptions;

public class ConsultaNaoEncontradaException extends IllegalArgumentException {
    public ConsultaNaoEncontradaException(String message) {
        super(message);
    }
}