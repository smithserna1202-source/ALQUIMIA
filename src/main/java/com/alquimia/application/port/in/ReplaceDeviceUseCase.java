package com.alquimia.application.port.in;

import com.alquimia.domain.model.DeviceInfo;

/** F4: reemplazo completo; distancia y fragmento son obligatorios. */
public interface ReplaceDeviceUseCase {
    DeviceInfo replace(String deviceId, Double distance, String messageFragment);
}
