package com.alquimia.infrastructure.rest;

import com.alquimia.application.dto.DeviceData;
import com.alquimia.application.port.in.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Adaptador de entrada HTTP. Solo traduce peticiones a casos de uso; los errores los maneja ApiExceptionHandler. */
@RestController
@RequestMapping("/api/alquimia")
public class AlquimiaController {

    private final ProcessCompleteUseCase processComplete;
    private final RegisterDeviceUseCase register;
    private final QueryDevicesUseCase query;
    private final ProcessAccumulatedUseCase processAccumulated;
    private final ReplaceDeviceUseCase replace;
    private final UpdateDevicePartiallyUseCase update;
    private final DeleteDeviceUseCase delete;

    public AlquimiaController(ProcessCompleteUseCase processComplete, RegisterDeviceUseCase register,
                              QueryDevicesUseCase query, ProcessAccumulatedUseCase processAccumulated,
                              ReplaceDeviceUseCase replace, UpdateDevicePartiallyUseCase update,
                              DeleteDeviceUseCase delete) {
        this.processComplete = processComplete;
        this.register = register;
        this.query = query;
        this.processAccumulated = processAccumulated;
        this.replace = replace;
        this.update = update;
        this.delete = delete;
    }

    /** F1 */
    @PostMapping("/process")
    public AlquimiaResponse process(@RequestBody List<DeviceRequest> body) {
        List<DeviceData> data = body.stream()
            .map(r -> r == null ? null : new DeviceData(r.deviceId(), r.distance(), r.messageFragment()))
            .toList();
        return AlquimiaResponse.from(processComplete.process(data));
    }

    /** F2 */
    @PostMapping("/devices")
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceResponse register(@RequestBody DeviceRequest r) {
        return DeviceResponse.from(register.register(new DeviceData(r.deviceId(), r.distance(), r.messageFragment())));
    }

    /** F3: consulta */
    @GetMapping("/devices")
    public List<DeviceResponse> list() {
        return query.findAll().stream().map(DeviceResponse::from).toList();
    }

    @GetMapping("/devices/{id:.+}")
    public DeviceResponse get(@PathVariable String id) {
        return DeviceResponse.from(query.findById(id));
    }

    /** F3: procesamiento acumulado */
    @GetMapping("/calculate")
    public AlquimiaResponse calculate() {
        return AlquimiaResponse.from(processAccumulated.processAccumulated());
    }

    /** F4 */
    @PutMapping("/devices/{id:.+}")
    public DeviceResponse replace(@PathVariable String id, @RequestBody DeviceUpdateRequest r) {
        return DeviceResponse.from(replace.replace(id, r.distance(), r.messageFragment()));
    }

    /** F5 */
    @PatchMapping("/devices/{id:.+}")
    public DeviceResponse patch(@PathVariable String id, @RequestBody DeviceUpdateRequest r) {
        return DeviceResponse.from(update.update(id, r.distance(), r.messageFragment()));
    }

    /** F6 */
    @DeleteMapping("/devices/{id:.+}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        delete.delete(id);
        return ResponseEntity.noContent().build();
    }
}
