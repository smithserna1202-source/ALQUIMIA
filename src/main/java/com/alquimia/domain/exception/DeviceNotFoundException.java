package com.alquimia.domain.exception;

/** El dispositivo es conocido pero no tiene información registrada. */
public class DeviceNotFoundException extends DomainException {
    public DeviceNotFoundException(String message) { super(message); }
}
