package com.alquimia.infrastructure.rest;

import com.alquimia.application.usecase.AlquimiaService;
import com.alquimia.domain.model.DeviceInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/alquimia")
public class AlquimiaController {

    /** Cuerpo de PUT/PATCH: campos opcionales (PUT exige distance). */
    public record DeviceUpdate(Double distance, String messageFragment) {}

    private final AlquimiaService service;

    public AlquimiaController(AlquimiaService service) { this.service = service; }

    @PostMapping("/process")
    public ResponseEntity<?> process(@RequestBody List<DeviceInfo> devices) {
        return run(() -> service.processComplete(devices));
    }

    @PostMapping("/devices")
    public ResponseEntity<?> register(@RequestBody DeviceInfo d) {
        return run(() -> { service.registerDevice(d); return Map.of("status", "Registrado"); });
    }

    @GetMapping("/calculate")
    public ResponseEntity<?> calculate() {
        return run(service::processAccumulated);
    }

    @PutMapping("/devices/{id}")
    public ResponseEntity<?> replace(@PathVariable String id, @RequestBody DeviceUpdate d) {
        return run(() -> {
            if (d.distance() == null) { throw new IllegalArgumentException("PUT requiere 'distance'."); }
            service.replaceDevice(id, d.distance(), d.messageFragment());
            return Map.of("status", "Reemplazado");
        });
    }

    @PatchMapping("/devices/{id}")
    public ResponseEntity<?> patch(@PathVariable String id, @RequestBody DeviceUpdate d) {
        return run(() -> { service.patchDevice(id, d.distance(), d.messageFragment()); return Map.of("status", "Actualizado"); });
    }

    @DeleteMapping("/devices/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return run(() -> { service.deleteDevice(id); return Map.of("status", "Eliminado"); });
    }

    private ResponseEntity<?> run(Supplier<Object> action) {
        try {
            return ResponseEntity.ok(action.get());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
