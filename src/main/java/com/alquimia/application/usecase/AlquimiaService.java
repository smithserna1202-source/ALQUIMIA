package com.alquimia.application.usecase;

import com.alquimia.domain.model.DeviceInfo;
import com.alquimia.domain.model.LabLocation;
import com.alquimia.domain.model.MessageReconstructor;
import com.alquimia.domain.model.TrilaterationCalculator;
import com.alquimia.domain.port.DeviceRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlquimiaService {
    private final DeviceRepositoryPort deviceRepository;

    public AlquimiaService(DeviceRepositoryPort deviceRepository) { this.deviceRepository = deviceRepository; }

    public record AlquimiaResponse(LabLocation location, String message) {}

    public AlquimiaResponse processComplete(List<DeviceInfo> devicesInput) {
        Map<String, DeviceInfo> map = new LinkedHashMap<>();
        devicesInput.forEach(d -> map.put(d.getDeviceId(), d));
        return buildResponse(map);
    }

    public void registerDevice(DeviceInfo device) { deviceRepository.save(device); }

    public AlquimiaResponse processAccumulated() {
        Map<String, DeviceInfo> accumulated = deviceRepository.findAll();
        if (accumulated.size() < 3) {
            throw new IllegalArgumentException("No hay datos suficientes (se requieren 3 dispositivos).");
        }
        return buildResponse(accumulated);
    }

    public void replaceDevice(String deviceId, double distance, String fragment) {
        if (deviceRepository.findById(deviceId).isEmpty()) {
            throw new IllegalArgumentException("Dispositivo no encontrado");
        }
        deviceRepository.update(new DeviceInfo(deviceId, distance, fragment));
    }

    /** Solo cambia los campos informados; null conserva el valor actual. */
    public void patchDevice(String deviceId, Double distance, String fragment) {
        DeviceInfo existing = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new IllegalArgumentException("Dispositivo no encontrado"));
        deviceRepository.update(new DeviceInfo(
            deviceId,
            distance != null ? distance : existing.getDistance(),
            fragment != null ? fragment : existing.getMessageFragment()));
    }

    public void deleteDevice(String deviceId) { deviceRepository.delete(deviceId); }

    private AlquimiaResponse buildResponse(Map<String, DeviceInfo> devices) {
        LabLocation location = TrilaterationCalculator.calculateLocation(devices);
        List<String> fragments = devices.values().stream().map(DeviceInfo::getMessageFragment).toList();
        return new AlquimiaResponse(location, MessageReconstructor.reconstruct(fragments));
    }
}
