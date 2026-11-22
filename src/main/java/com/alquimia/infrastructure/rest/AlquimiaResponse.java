package com.alquimia.infrastructure.rest;

import com.alquimia.application.dto.AlquimiaResult;

public record AlquimiaResponse(Location location, String message) {
    public record Location(double x, double y) {}

    static AlquimiaResponse from(AlquimiaResult r) {
        return new AlquimiaResponse(new Location(r.location().x(), r.location().y()), r.message());
    }
}
