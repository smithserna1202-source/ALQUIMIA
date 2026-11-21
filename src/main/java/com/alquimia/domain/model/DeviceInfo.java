package com.alquimia.domain.model;

public record DeviceInfo(String deviceId, double distance, String messageFragment) {
    public DeviceInfo {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("El deviceId es obligatorio.");
        }
        if (Double.isNaN(distance) || distance < 0) {
            throw new IllegalArgumentException("La distancia debe ser un numero mayor o igual a 0.");
        }
        messageFragment = messageFragment == null ? "" : messageFragment;
    }

    // Se conservan los getters del proyecto original.
    public String getDeviceId() { return deviceId; }
    public double getDistance() { return distance; }
    public String getMessageFragment() { return messageFragment; }
}
