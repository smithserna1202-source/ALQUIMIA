package com.alquimia.domain.model;

import java.util.Map;

public class TrilaterationCalculator {

    private static final Map<String, double[]> DEVICE_POSITIONS = Map.of(
        "STAR Labs", new double[]{-500.0, -200.0},
        "CCPD", new double[]{100.0, -100.0},
        "S.T.A.R. Satellite", new double[]{500.0, 100.0}
    );

    public static LabLocation calculateLocation(Map<String, DeviceInfo> devices) {
        if (devices.size() < 3) {
            throw new IllegalArgumentException("Se necesitan datos de al menos 3 dispositivos para la trilateración.");
        }
        double[] p1 = DEVICE_POSITIONS.get("STAR Labs");
        double[] p2 = DEVICE_POSITIONS.get("CCPD");
        double[] p3 = DEVICE_POSITIONS.get("S.T.A.R. Satellite");
        double r1 = distanceOf(devices, "STAR Labs");
        double r2 = distanceOf(devices, "CCPD");
        double r3 = distanceOf(devices, "S.T.A.R. Satellite");

        double A = 2 * p2[0] - 2 * p1[0];
        double B = 2 * p2[1] - 2 * p1[1];
        double C = Math.pow(r1, 2) - Math.pow(r2, 2) - Math.pow(p1[0], 2) + Math.pow(p2[0], 2) - Math.pow(p1[1], 2) + Math.pow(p2[1], 2);
        double D = 2 * p3[0] - 2 * p2[0];
        double E = 2 * p3[1] - 2 * p2[1];
        double F = Math.pow(r2, 2) - Math.pow(r3, 2) - Math.pow(p2[0], 2) + Math.pow(p3[0], 2) - Math.pow(p2[1], 2) + Math.pow(p3[1], 2);

        double det = A * E - B * D;
        if (Math.abs(det) < 1e-9) {
            throw new IllegalStateException("Los dispositivos estan alineados, no se puede calcular.");
        }
        double x = (C * E - F * B) / det;
        double y = (A * F - C * D) / det;
        return new LabLocation(Math.round(x * 100.0) / 100.0, Math.round(y * 100.0) / 100.0);
    }

    private static double distanceOf(Map<String, DeviceInfo> devices, String id) {
        DeviceInfo d = devices.get(id);
        if (d == null) {
            throw new IllegalArgumentException("Falta el dispositivo '" + id + "'.");
        }
        return d.getDistance();
    }
}
