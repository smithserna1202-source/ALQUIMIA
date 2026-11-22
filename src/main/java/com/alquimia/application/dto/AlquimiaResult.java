package com.alquimia.application.dto;

import com.alquimia.domain.model.LabLocation;

public record AlquimiaResult(LabLocation location, String message) {}
