package com.alquimia.application;

import static org.junit.jupiter.api.Assertions.*;

import com.alquimia.application.dto.AlquimiaResult;
import com.alquimia.application.dto.DeviceData;
import com.alquimia.application.usecase.AlquimiaService;
import com.alquimia.domain.exception.*;
import com.alquimia.domain.model.DeviceInfo;
import com.alquimia.infrastructure.adapter.InMemoryDeviceRepositoryAdapter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlquimiaServiceTest {

    private AlquimiaService service;

    @BeforeEach
    void setUp() { service = new AlquimiaService(new InMemoryDeviceRepositoryAdapter()); }

    private static DeviceData d(String id, Double dist, String frag) { return new DeviceData(id, dist, frag); }

    private void registerAllInAnyOrder() {
        service.register(d("S.T.A.R. Satellite", 509.90, "Alquimia"));
        service.register(d("STAR Labs", 538.52, "Hola"));
        service.register(d("CCPD", 141.42, ""));
    }

    @Test
    void f1ProcessesCompleteInformation() {
        AlquimiaResult r = service.process(List.of(
            d("STAR Labs", 538.52, "Hola"), d("CCPD", 141.42, ""), d("S.T.A.R. Satellite", 509.90, "Alquimia")));
        assertEquals("Hola Alquimia", r.message());
        assertEquals(0.0, r.location().x(), 0.5);
    }

    @Test
    void f1RejectsDuplicatesUnknownAndMissingDevices() {
        assertThrows(InvalidDeviceDataException.class, () -> service.process(List.of(
            d("CCPD", 1.0, ""), d("CCPD", 2.0, ""), d("STAR Labs", 1.0, ""))));
        assertThrows(UnknownDeviceException.class, () -> service.process(List.of(
            d("Otro", 1.0, ""), d("CCPD", 1.0, ""), d("STAR Labs", 1.0, ""))));
        assertThrows(InsufficientDataException.class, () -> service.process(List.of(d("CCPD", 1.0, ""))));
    }

    @Test
    void f2RegistersInAnyOrderAndRejectsDuplicatesAndInvalidData() {
        registerAllInAnyOrder();
        assertEquals(3, service.findAll().size());
        assertThrows(DeviceAlreadyRegisteredException.class, () -> service.register(d("CCPD", 1.0, "x")));
        assertThrows(UnknownDeviceException.class, () -> service.register(d("Otro", 1.0, "x")));
        assertThrows(InvalidDeviceDataException.class, () -> service.register(d("CCPD", null, "x")));
    }

    @Test
    void f3ReportsInsufficientDataThenProcessesAccumulated() {
        service.register(d("CCPD", 141.42, "uno"));
        assertThrows(InsufficientDataException.class, () -> service.processAccumulated());
        service.register(d("STAR Labs", 538.52, "Hola"));
        service.register(d("S.T.A.R. Satellite", 509.90, "Alquimia"));
        assertEquals("Hola uno Alquimia", service.processAccumulated().message());
    }

    @Test
    void f4ReplacesOnlyThatDeviceAndNeedsBothFields() {
        registerAllInAnyOrder();
        service.replace("STAR Labs", 10.0, "nuevo");
        assertEquals(new DeviceInfo(com.alquimia.domain.model.KnownDevice.STAR_LABS, 10.0, "nuevo"), service.findById("STAR Labs"));
        assertEquals(141.42, service.findById("CCPD").distance());
        assertThrows(InvalidDeviceDataException.class, () -> service.replace("CCPD", 5.0, null));
        assertThrows(InvalidDeviceDataException.class, () -> service.replace("CCPD", null, "x"));
    }

    @Test
    void f4AndF5AndF6FailForUnregisteredDevice() {
        assertThrows(DeviceNotFoundException.class, () -> service.replace("CCPD", 1.0, "x"));
        assertThrows(DeviceNotFoundException.class, () -> service.update("CCPD", 1.0, null));
        assertThrows(DeviceNotFoundException.class, () -> service.delete("CCPD"));
    }

    @Test
    void f5UpdatesOnlyProvidedFields() {
        registerAllInAnyOrder();
        service.update("STAR Labs", 99.0, null);
        assertEquals("Hola", service.findById("STAR Labs").messageFragment());
        assertEquals(99.0, service.findById("STAR Labs").distance());
        service.update("STAR Labs", null, "chau");
        assertEquals(99.0, service.findById("STAR Labs").distance());
        assertEquals("chau", service.findById("STAR Labs").messageFragment());
        assertThrows(InvalidDeviceDataException.class, () -> service.update("STAR Labs", null, null));
        assertThrows(InvalidDeviceDataException.class, () -> service.update("STAR Labs", -3.0, null));
    }

    @Test
    void f6DeletesOnlyThatDevice() {
        registerAllInAnyOrder();
        service.delete("CCPD");
        assertEquals(2, service.findAll().size());
        assertThrows(DeviceNotFoundException.class, () -> service.findById("CCPD"));
        assertThrows(InsufficientDataException.class, () -> service.processAccumulated());
        service.register(d("CCPD", 141.42, "")); // se puede volver a registrar
    }
}
