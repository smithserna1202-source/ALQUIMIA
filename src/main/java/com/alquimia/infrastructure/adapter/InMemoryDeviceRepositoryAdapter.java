package com.alquimia.infrastructure.adapter;

import com.alquimia.application.port.out.DeviceRepositoryPort;
import com.alquimia.domain.model.DeviceInfo;
import com.alquimia.domain.model.KnownDevice;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Adaptador de salida: guarda en memoria (sin base de datos). EnumMap mantiene el orden canónico. */
public class InMemoryDeviceRepositoryAdapter implements DeviceRepositoryPort {

    private final Map<KnownDevice, DeviceInfo> storage = new EnumMap<>(KnownDevice.class);

    @Override
    public synchronized void save(DeviceInfo info) { storage.put(info.device(), info); }

    @Override
    public synchronized Optional<DeviceInfo> findById(KnownDevice device) { return Optional.ofNullable(storage.get(device)); }

    @Override
    public synchronized List<DeviceInfo> findAll() { return List.copyOf(storage.values()); }

    @Override
    public synchronized boolean deleteById(KnownDevice device) { return storage.remove(device) != null; }
}
