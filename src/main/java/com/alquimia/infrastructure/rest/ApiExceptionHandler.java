package com.alquimia.infrastructure.rest;

import com.alquimia.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** Único sitio donde los errores de dominio se convierten en códigos HTTP. */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({InvalidDeviceDataException.class, UnknownDeviceException.class})
    ResponseEntity<Map<String, String>> badRequest(DomainException e) { return body(HttpStatus.BAD_REQUEST, e.getMessage()); }

    @ExceptionHandler(DeviceNotFoundException.class)
    ResponseEntity<Map<String, String>> notFound(DomainException e) { return body(HttpStatus.NOT_FOUND, e.getMessage()); }

    @ExceptionHandler(DeviceAlreadyRegisteredException.class)
    ResponseEntity<Map<String, String>> conflict(DomainException e) { return body(HttpStatus.CONFLICT, e.getMessage()); }

    @ExceptionHandler({InsufficientDataException.class, TrilaterationException.class})
    ResponseEntity<Map<String, String>> unprocessable(DomainException e) { return body(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage()); }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, String>> unreadable(HttpMessageNotReadableException e) {
        return body(HttpStatus.BAD_REQUEST, "El cuerpo de la petición falta o no es un JSON válido (revise tipos y campos).");
    }

    private ResponseEntity<Map<String, String>> body(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}
