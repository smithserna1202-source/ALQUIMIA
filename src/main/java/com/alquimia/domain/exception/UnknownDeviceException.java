package com.alquimia.domain.exception;

/** El identificador no corresponde a un dispositivo de vigilancia conocido. */
public class UnknownDeviceException extends DomainException {
    public UnknownDeviceException(String message) { super(message); }
}
