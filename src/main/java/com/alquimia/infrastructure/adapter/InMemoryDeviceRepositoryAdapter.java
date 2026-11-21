package com.alquimia.infrastructure.adapter;

import com.alquimia.domain.model.DeviceInfo;
import com.alquimia.domain.port.DeviceRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryDeviceRepositoryAdapter implements DeviceRepositoryPort {

    // LinkedHashMap conserva el orden de llegada; synchronized porque Spring atiende peticiones en paralelo.
    private final Map<String, DeviceInfo> storage = new LinkedHashMap<>();

    @Override
    public synchronized void save(DeviceInfo device) { storage.put(device.getDeviceId(), device); }

    @Override
    public synchronized Optional<DeviceInfo> findById(String deviceId) { return Optional.ofNullable(storage.get(deviceId)); }

    @Override
    public synchronized Map<String, DeviceInfo> findAll() { return new LinkedHashMap<>(storage); }

    @Override
    public synchronized void delete(String deviceId) { storage.remove(deviceId); }

    @Override
    public synchronized void update(DeviceInfo device) { storage.put(device.getDeviceId(), device); }
}
