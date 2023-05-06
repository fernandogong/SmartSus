package br.fiap.smartsus.exceptions;

public record RestError(
    int cod,
    String message
) {
    
}
