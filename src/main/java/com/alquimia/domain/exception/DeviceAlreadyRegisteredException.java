package com.alquimia.domain.exception;

/** Se intentó registrar un dispositivo que ya tiene información. */
public class DeviceAlreadyRegisteredException extends DomainException {
    public DeviceAlreadyRegisteredException(String message) { super(message); }
}
