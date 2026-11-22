package com.alquimia.domain.model;

import com.alquimia.domain.exception.InsufficientDataException;
import com.alquimia.domain.exception.TrilaterationException;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Servicio de dominio: ubica el laboratorio restando las ecuaciones de las tres circunferencias. */
public final class TrilaterationCalculator {

    private TrilaterationCalculator() {}

    public static LabLocation calculateLocation(Collection<DeviceInfo> infos) {
        Map<KnownDevice, DeviceInfo> byDevice = new EnumMap<>(KnownDevice.class);
        infos.forEach(i -> byDevice.put(i.device(), i));

        String missing = java.util.Arrays.stream(KnownDevice.values())
            .filter(d -> !byDevice.containsKey(d))
            .map(KnownDevice::displayName)
            .collect(Collectors.joining(", "));
        if (!missing.isEmpty()) {
            throw new InsufficientDataException("No hay datos suficientes: faltan " + missing + ".");
        }

        KnownDevice d1 = KnownDevice.STAR_LABS, d2 = KnownDevice.CCPD, d3 = KnownDevice.STAR_SATELLITE;
        double r1 = byDevice.get(d1).distance(), r2 = byDevice.get(d2).distance(), r3 = byDevice.get(d3).distance();

        double a = 2 * (d2.x() - d1.x());
        double b = 2 * (d2.y() - d1.y());
        double c = r1 * r1 - r2 * r2 - d1.x() * d1.x() + d2.x() * d2.x() - d1.y() * d1.y() + d2.y() * d2.y();
        double d = 2 * (d3.x() - d2.x());
        double e = 2 * (d3.y() - d2.y());
        double f = r2 * r2 - r3 * r3 - d2.x() * d2.x() + d3.x() * d3.x() - d2.y() * d2.y() + d3.y() * d3.y();

        double det = a * e - b * d;
        if (Math.abs(det) < 1e-9) {
            throw new TrilaterationException("Los dispositivos están alineados, no se puede calcular.");
        }
        double x = (c * e - f * b) / det;
        double y = (a * f - c * d) / det;
        return new LabLocation(Math.round(x * 100.0) / 100.0, Math.round(y * 100.0) / 100.0);
    }
}
