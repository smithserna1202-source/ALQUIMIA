package com.alquimia.domain.exception;

/** Datos del dispositivo ausentes o inválidos. */
public class InvalidDeviceDataException extends DomainException {
    public InvalidDeviceDataException(String message) { super(message); }
}
