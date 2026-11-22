package com.alquimia.domain.model;

import com.alquimia.domain.exception.InvalidDeviceDataException;
import com.alquimia.domain.exception.UnknownDeviceException;

import java.util.Arrays;

/** Catálogo de los tres dispositivos de vigilancia. El orden de declaración es el orden canónico del mensaje. */
public enum KnownDevice {
    STAR_LABS("STAR Labs", -500.0, -200.0),
    CCPD("CCPD", 100.0, -100.0),
    STAR_SATELLITE("S.T.A.R. Satellite", 500.0, 100.0);

    private final String displayName;
    private final double x;
    private final double y;

    KnownDevice(String displayName, double x, double y) {
        this.displayName = displayName;
        this.x = x;
        this.y = y;
    }

    public String displayName() { return displayName; }
    public double x() { return x; }
    public double y() { return y; }

    public static KnownDevice fromName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidDeviceDataException("El deviceId es obligatorio.");
        }
        String wanted = name.trim();
        return Arrays.stream(values())
            .filter(d -> d.displayName.equalsIgnoreCase(wanted))
            .findFirst()
            .orElseThrow(() -> new UnknownDeviceException("Dispositivo desconocido: '" + wanted + "'."));
    }
}
