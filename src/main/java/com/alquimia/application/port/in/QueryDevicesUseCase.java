package com.alquimia.application.port.in;

import com.alquimia.domain.model.DeviceInfo;
import java.util.List;

/** F3 (consulta): lo que hay registrado, de todos los dispositivos o de uno. */
public interface QueryDevicesUseCase {
    List<DeviceInfo> findAll();
    DeviceInfo findById(String deviceId);
}
