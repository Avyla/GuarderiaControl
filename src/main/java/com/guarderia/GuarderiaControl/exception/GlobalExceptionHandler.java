package com.guarderia.GuarderiaControl.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PagoNotFoundException.class)
    public ResponseEntity<String> handlePagoNotFound(PagoNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(PadreNotFoundException.class)
    public ResponseEntity<String> handlePadreNotFound(PadreNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NinoNotFoundException.class)
    public ResponseEntity<String> handleNinoNotFound(NinoNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        // Verifica si la causa es una deserialización inválida del enum
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            // Extrae el tipo del campo y los valores permitidos
            if (cause.getTargetType().isEnum()) {
                String[] acceptedValues = Arrays.stream(cause.getTargetType().getEnumConstants())
                        .map(Object::toString)
                        .toArray(String[]::new);
                String errorMessage = String.format(
                        "El valor '%s' no es válido. Los valores permitidos son: %s.",
                        cause.getValue(), String.join(", ", acceptedValues)
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", errorMessage));
            }
        }

        // Respuesta genérica si no es un problema relacionado con un enum
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Solicitud no legible"));
    }
}
