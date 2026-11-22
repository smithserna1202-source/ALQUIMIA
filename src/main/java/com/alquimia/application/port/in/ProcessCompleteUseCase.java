package com.alquimia.application.port.in;

import com.alquimia.application.dto.AlquimiaResult;
import com.alquimia.application.dto.DeviceData;
import java.util.List;

/** F1: procesa la información completa de los tres dispositivos sin guardarla. */
public interface ProcessCompleteUseCase {
    AlquimiaResult process(List<DeviceData> devices);
}
