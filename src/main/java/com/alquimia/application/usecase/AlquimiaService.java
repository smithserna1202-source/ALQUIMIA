package com.alquimia.application.usecase;

import com.alquimia.application.dto.AlquimiaResult;
import com.alquimia.application.dto.DeviceData;
import com.alquimia.application.port.in.*;
import com.alquimia.application.port.out.DeviceRepositoryPort;
import com.alquimia.domain.exception.DeviceAlreadyRegisteredException;
import com.alquimia.domain.exception.DeviceNotFoundException;
import com.alquimia.domain.exception.InsufficientDataException;
import com.alquimia.domain.exception.InvalidDeviceDataException;
import com.alquimia.domain.model.*;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Orquesta los casos de uso F1-F6. No usa anotaciones de framework: se cablea en infrastructure.config.
 * Las operaciones que leen y luego escriben son synchronized para que sean atómicas entre peticiones.
 */
public class AlquimiaService implements ProcessCompleteUseCase, RegisterDeviceUseCase, QueryDevicesUseCase,
        ProcessAccumulatedUseCase, ReplaceDeviceUseCase, UpdateDevicePartiallyUseCase, DeleteDeviceUseCase {

    private final DeviceRepositoryPort repository;

    public AlquimiaService(DeviceRepositoryPort repository) { this.repository = repository; }

    @Override
    public AlquimiaResult process(List<DeviceData> devices) {
        if (devices == null || devices.isEmpty()) {
            throw new InsufficientDataException("No hay datos suficientes: se requieren los 3 dispositivos.");
        }
        Set<KnownDevice> seen = EnumSet.noneOf(KnownDevice.class);
        List<DeviceInfo> infos = devices.stream().map(this::toDeviceInfo).toList();
        for (DeviceInfo info : infos) {
            if (!seen.add(info.device())) {
                throw new InvalidDeviceDataException("Dispositivo repetido: '" + info.device().displayName() + "'.");
            }
        }
        return buildResult(infos);
    }

    @Override
    public synchronized DeviceInfo register(DeviceData data) {
        DeviceInfo info = toDeviceInfo(data);
        if (repository.findById(info.device()).isPresent()) {
            throw new DeviceAlreadyRegisteredException("'" + info.device().displayName()
                + "' ya tiene información registrada; use reemplazo (PUT) o actualización parcial (PATCH).");
        }
        repository.save(info);
        return info;
    }

    @Override
    public List<DeviceInfo> findAll() { return repository.findAll(); }

    @Override
    public DeviceInfo findById(String deviceId) { return getRegistered(KnownDevice.fromName(deviceId)); }

    @Override
    public AlquimiaResult processAccumulated() { return buildResult(repository.findAll()); }

    @Override
    public synchronized DeviceInfo replace(String deviceId, Double distance, String messageFragment) {
        DeviceInfo replacement = DeviceInfo.of(deviceId, distance, messageFragment);
        getRegistered(replacement.device());
        repository.save(replacement);
        return replacement;
    }

    @Override
    public synchronized DeviceInfo update(String deviceId, Double distance, String messageFragment) {
        DeviceInfo updated = getRegistered(KnownDevice.fromName(deviceId)).updatedWith(distance, messageFragment);
        repository.save(updated);
        return updated;
    }

    @Override
    public synchronized void delete(String deviceId) {
        KnownDevice device = KnownDevice.fromName(deviceId);
        if (!repository.deleteById(device)) {
            throw notRegistered(device);
        }
    }

    private DeviceInfo toDeviceInfo(DeviceData data) {
        if (data == null) {
            throw new InvalidDeviceDataException("Se recibió un dispositivo vacío.");
        }
        return DeviceInfo.of(data.deviceId(), data.distance(), data.messageFragment());
    }

    private DeviceInfo getRegistered(KnownDevice device) {
        return repository.findById(device).orElseThrow(() -> notRegistered(device));
    }

    private DeviceNotFoundException notRegistered(KnownDevice device) {
        return new DeviceNotFoundException("'" + device.displayName() + "' no tiene información registrada.");
    }

    private AlquimiaResult buildResult(Collection<DeviceInfo> infos) {
        LabLocation location = TrilaterationCalculator.calculateLocation(infos);
        return new AlquimiaResult(location, MessageReconstructor.reconstruct(infos));
    }
}
