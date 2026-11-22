package com.alquimia.application.port.in;

import com.alquimia.domain.model.DeviceInfo;

/** F5: actualización parcial; un campo null conserva su valor anterior. */
public interface UpdateDevicePartiallyUseCase {
    DeviceInfo update(String deviceId, Double distance, String messageFragment);
}
