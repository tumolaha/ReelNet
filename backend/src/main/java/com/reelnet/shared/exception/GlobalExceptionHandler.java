package com.reelnet.shared.exception;

import com.reelnet.infrastructure.monitoring.exception.HealthCheckException;
import com.reelnet.shared.dto.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HealthCheckException.class)
    public ResponseEntity<ApiResponse<Void>> handleHealthCheckException(
            HealthCheckException ex) {
        log.error("Health check error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.<Void>builder()
                        .status("ERROR")
                        .error(ApiResponse.ErrorDetails.builder()
                                .message(ex.getMessage())
                                .code(ex.getCode())
                                .details(ex.getDetails())
                                .build())
                        .meta(createMetaData())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(
            ApiResponse.<Map<String, String>>builder()
                .status("ERROR")
                .error(ApiResponse.ErrorDetails.builder()
                    .message("Validation failed")
                    .code("VALIDATION_ERROR")
                    .details(errors)
                    .build())
                .meta(createMetaData())
                .build()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFound(
            EntityNotFoundException ex) {
        log.error("Entity not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiResponse.<Void>builder()
                .status("ERROR")
                .error(ApiResponse.ErrorDetails.builder()
                    .message(ex.getMessage())
                    .code("RESOURCE_NOT_FOUND")
                    .build())
                .meta(createMetaData())
                .build()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiResponse.<Void>builder()
                .status("ERROR")
                .error(ApiResponse.ErrorDetails.builder()
                    .message("Data integrity violation")
                    .code("DATA_INTEGRITY_ERROR")
                    .details(ex.getMostSpecificCause().getMessage())
                    .build())
                .meta(createMetaData())
                .build()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            AccessDeniedException ex) {
        log.error("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ApiResponse.<Void>builder()
                .status("ERROR")
                .error(ApiResponse.ErrorDetails.builder()
                    .message("Access denied")
                    .code("ACCESS_DENIED")
                    .build())
                .meta(createMetaData())
                .build()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(
            BadCredentialsException ex) {
        log.error("Invalid credentials: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiResponse.<Void>builder()
                .status("ERROR")
                .error(ApiResponse.ErrorDetails.builder()
                    .message("Invalid credentials")
                    .code("INVALID_CREDENTIALS")
                    .build())
                .meta(createMetaData())
                .build()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<Void>> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiResponse.<Void>builder()
                .status("ERROR")
                .error(ApiResponse.ErrorDetails.builder()
                    .message("An unexpected error occurred")
                    .code("INTERNAL_SERVER_ERROR")
                    .details(ex.getMessage())
                    .build())
                .meta(createMetaData())
                .build()
        );
    }

    private ApiResponse.MetaData createMetaData() {
        return ApiResponse.MetaData.builder()
                .requestId(UUID.randomUUID().toString())
                .build();
    }
} 