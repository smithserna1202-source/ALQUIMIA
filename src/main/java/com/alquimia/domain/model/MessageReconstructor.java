package com.alquimia.domain.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Servicio de dominio: une los fragmentos ignorando los vacíos. El orden es el canónico del catálogo de
 * dispositivos, no el de llegada, para que el mensaje no dependa de quién envió primero.
 */
public final class MessageReconstructor {

    private MessageReconstructor() {}

    public static String reconstruct(Collection<DeviceInfo> infos) {
        return infos.stream()
            .sorted(Comparator.comparing(DeviceInfo::device))
            .map(DeviceInfo::messageFragment)
            .filter(fragment -> !fragment.isBlank())
            .collect(Collectors.joining(" "));
    }
}
