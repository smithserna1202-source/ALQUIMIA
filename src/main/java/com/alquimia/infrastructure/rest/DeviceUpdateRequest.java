package com.alquimia.infrastructure.rest;

/** Cuerpo de PUT (ambos obligatorios) y PATCH (opcionales). */
public record DeviceUpdateRequest(Double distance, String messageFragment) {}
