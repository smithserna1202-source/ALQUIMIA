package com.alquimia.application.port.out;

import com.alquimia.domain.model.DeviceInfo;
import com.alquimia.domain.model.KnownDevice;
import java.util.List;
import java.util.Optional;

/** Puerto de salida: almacenamiento temporal de la información por dispositivo. */
public interface DeviceRepositoryPort {
    /** Inserta o sustituye por completo lo guardado para ese dispositivo. */
    void save(DeviceInfo info);
    Optional<DeviceInfo> findById(KnownDevice device);
    /** Copia de lo registrado, en el orden canónico de los dispositivos. */
    List<DeviceInfo> findAll();
    /** @return true si había información y fue eliminada */
    boolean deleteById(KnownDevice device);
}
