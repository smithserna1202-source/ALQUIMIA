package com.alquimia.application.port.in;

import com.alquimia.application.dto.DeviceData;
import com.alquimia.domain.model.DeviceInfo;

/** F2: registra la información de un dispositivo (si ya existe, usar reemplazo o actualización). */
public interface RegisterDeviceUseCase {
    DeviceInfo register(DeviceData data);
}
