package com.alquimia.application.dto;

/** Datos crudos de un dispositivo tal como llegan a la aplicación (aún sin validar). */
public record DeviceData(String deviceId, Double distance, String messageFragment) {}
