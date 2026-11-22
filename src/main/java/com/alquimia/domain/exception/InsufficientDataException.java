package com.alquimia.domain.exception;

/** No hay datos suficientes para calcular la ubicación o el mensaje. */
public class InsufficientDataException extends DomainException {
    public InsufficientDataException(String message) { super(message); }
}
