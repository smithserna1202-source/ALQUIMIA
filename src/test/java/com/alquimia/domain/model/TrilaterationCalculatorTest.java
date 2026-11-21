package com.alquimia.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TrilaterationCalculatorTest {

    private static Map<String, DeviceInfo> devices(double r1, double r2, double r3) {
        Map<String, DeviceInfo> m = new LinkedHashMap<>();
        m.put("STAR Labs", new DeviceInfo("STAR Labs", r1, "a"));
        m.put("CCPD", new DeviceInfo("CCPD", r2, "b"));
        m.put("S.T.A.R. Satellite", new DeviceInfo("S.T.A.R. Satellite", r3, "c"));
        return m;
    }

    @Test
    void locatesLabAtKnownPoint() {
        // Laboratorio real en (0, 0)
        LabLocation p = TrilaterationCalculator.calculateLocation(devices(538.52, 141.42, 509.90));
        assertEquals(0.0, p.x(), 0.5);
        assertEquals(0.0, p.y(), 0.5);
    }

    @Test
    void failsWhenADeviceIsMissing() {
        Map<String, DeviceInfo> m = devices(1, 1, 1);
        m.remove("CCPD");
        m.put("Otro", new DeviceInfo("Otro", 1, ""));
        assertThrows(IllegalArgumentException.class, () -> TrilaterationCalculator.calculateLocation(m));
    }

    @Test
    void rebuildsMessageIgnoringEmptyFragments() {
        assertEquals("hola mundo", MessageReconstructor.reconstruct(List.of(" hola ", "", "mundo")));
    }
}
