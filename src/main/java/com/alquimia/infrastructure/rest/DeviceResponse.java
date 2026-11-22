package com.alquimia.infrastructure.rest;

import com.alquimia.domain.model.DeviceInfo;

public record DeviceResponse(String deviceId, double distance, String messageFragment) {
    static DeviceResponse from(DeviceInfo i) {
        return new DeviceResponse(i.device().displayName(), i.distance(), i.messageFragment());
    }
}
