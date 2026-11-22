package com.alquimia.domain.exception;

/** Las distancias no permiten calcular una ubicación (geometría degenerada). */
public class TrilaterationException extends DomainException {
    public TrilaterationException(String message) { super(message); }
}
