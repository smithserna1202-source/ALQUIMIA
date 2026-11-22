package com.alquimia.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.alquimia.domain.exception.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class DomainTest {

    private static DeviceInfo info(KnownDevice d, double dist, String frag) { return new DeviceInfo(d, dist, frag); }

    @Test
    void locatesLabAtKnownPoint() {
        LabLocation p = TrilaterationCalculator.calculateLocation(List.of(
            info(KnownDevice.STAR_LABS, 538.52, "a"), info(KnownDevice.CCPD, 141.42, "b"),
            info(KnownDevice.STAR_SATELLITE, 509.90, "c")));
        assertEquals(0.0, p.x(), 0.5);
        assertEquals(0.0, p.y(), 0.5);
    }

    @Test
    void trilaterationFailsWithoutAllThreeDevices() {
        assertThrows(InsufficientDataException.class, () -> TrilaterationCalculator.calculateLocation(List.of(
            info(KnownDevice.STAR_LABS, 1, "a"), info(KnownDevice.CCPD, 1, "b"))));
    }

    @Test
    void messageIgnoresEmptyFragmentsAndUsesCanonicalOrder() {
        assertEquals("hola mundo", MessageReconstructor.reconstruct(List.of(
            info(KnownDevice.STAR_SATELLITE, 1, "mundo"), info(KnownDevice.CCPD, 1, ""),
            info(KnownDevice.STAR_LABS, 1, " hola "))));
    }

    @Test
    void rejectsInvalidData() {
        assertThrows(UnknownDeviceException.class, () -> DeviceInfo.of("Otro", 1.0, ""));
        assertThrows(InvalidDeviceDataException.class, () -> DeviceInfo.of("CCPD", null, ""));
        assertThrows(InvalidDeviceDataException.class, () -> DeviceInfo.of("CCPD", -1.0, ""));
        assertThrows(InvalidDeviceDataException.class, () -> DeviceInfo.of("CCPD", Double.NaN, ""));
        assertThrows(InvalidDeviceDataException.class, () -> DeviceInfo.of("CCPD", 1.0, null));
        assertThrows(InvalidDeviceDataException.class, () -> DeviceInfo.of(" ", 1.0, ""));
    }

    @Test
    void partialUpdateKeepsOmittedFieldsAndNeedsAtLeastOne() {
        DeviceInfo base = info(KnownDevice.CCPD, 10, "hola");
        assertEquals(new DeviceInfo(KnownDevice.CCPD, 20, "hola"), base.updatedWith(20.0, null));
        assertEquals(new DeviceInfo(KnownDevice.CCPD, 10, "adios"), base.updatedWith(null, "adios"));
        assertThrows(InvalidDeviceDataException.class, () -> base.updatedWith(null, null));
    }
}
