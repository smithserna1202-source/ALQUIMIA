package com.alquimia.application.port.in;

/** F6: elimina por completo la información de un dispositivo. */
public interface DeleteDeviceUseCase {
    void delete(String deviceId);
}
