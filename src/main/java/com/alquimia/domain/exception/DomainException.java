package com.alquimia.domain.exception;

/** Base de los errores de negocio. No conoce HTTP: la infraestructura decide cómo mostrarlos. */
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) { super(message); }
}
