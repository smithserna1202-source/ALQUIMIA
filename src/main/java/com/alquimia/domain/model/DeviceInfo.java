package com.alquimia.domain.model;

import com.alquimia.domain.exception.InvalidDeviceDataException;

/**
 * Información interceptada por un dispositivo. Es inmutable: reemplazar o actualizar produce una instancia nueva,
 * así los datos de un dispositivo nunca se mezclan con los de otro.
 */
public record DeviceInfo(KnownDevice device, double distance, String messageFragment) {

    public DeviceInfo {
        if (device == null) {
            throw new InvalidDeviceDataException("El dispositivo es obligatorio.");
        }
        if (!Double.isFinite(distance) || distance < 0) {
            throw new InvalidDeviceDataException("La distancia debe ser un número finito mayor o igual a 0.");
        }
        if (messageFragment == null) {
            throw new InvalidDeviceDataException("El fragmento del mensaje es obligatorio (use \"\" si no se recibió nada).");
        }
        messageFragment = messageFragment.trim();
    }

    /** Alta o reemplazo completo: distancia y fragmento son obligatorios. */
    public static DeviceInfo of(String deviceName, Double distance, String fragment) {
        KnownDevice device = KnownDevice.fromName(deviceName);
        if (distance == null) {
            throw new InvalidDeviceDataException("La distancia es obligatoria.");
        }
        return new DeviceInfo(device, distance, fragment);
    }

    /** Actualización parcial: solo cambia lo informado (null = conservar) y exige al menos un campo. */
    public DeviceInfo updatedWith(Double newDistance, String newFragment) {
        if (newDistance == null && newFragment == null) {
            throw new InvalidDeviceDataException("Debe enviar al menos 'distance' o 'messageFragment'.");
        }
        return new DeviceInfo(
            device,
            newDistance != null ? newDistance : distance,
            newFragment != null ? newFragment : messageFragment);
    }
}
