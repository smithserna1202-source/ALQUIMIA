package com.alquimia.infrastructure.rest;

/** Cuerpo de POST /devices y de cada elemento de POST /process. Los campos son objetos para detectar los ausentes. */
public record DeviceRequest(String deviceId, Double distance, String messageFragment) {}
