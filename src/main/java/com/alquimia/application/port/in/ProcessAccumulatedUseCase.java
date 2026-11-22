package com.alquimia.application.port.in;

import com.alquimia.application.dto.AlquimiaResult;

/** F3 (procesamiento): usa lo acumulado; falla con "datos insuficientes" si faltan dispositivos. */
public interface ProcessAccumulatedUseCase {
    AlquimiaResult processAccumulated();
}
