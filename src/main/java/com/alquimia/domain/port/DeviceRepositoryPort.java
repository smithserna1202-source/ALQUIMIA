package com.alquimia.domain.port;

import com.alquimia.domain.model.DeviceInfo;
import java.util.Map;
import java.util.Optional;

public interface DeviceRepositoryPort {
    void save(DeviceInfo device);
    Optional<DeviceInfo> findById(String deviceId);
    Map<String, DeviceInfo> findAll();
    void delete(String deviceId);
    void update(DeviceInfo device);
}
