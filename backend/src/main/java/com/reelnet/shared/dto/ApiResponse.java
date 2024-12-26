package com.reelnet.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private ErrorDetails error;
    private MetaData meta;
    
    @Builder.Default
    private Long timestamp = Instant.now().toEpochMilli();

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("SUCCESS")
                .data(data)
                .meta(MetaData.builder().build())
                .timestamp(getCurrentTimestamp())
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status("SUCCESS")
                .message(message)
                .data(data)
                .meta(MetaData.builder().build())
                .timestamp(getCurrentTimestamp())
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("ERROR")
                .error(ErrorDetails.builder()
                        .message(message)
                        .build())
                .meta(MetaData.builder().build())
                .timestamp(getCurrentTimestamp())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return ApiResponse.<T>builder()
                .status("ERROR")
                .error(ErrorDetails.builder()
                        .message(message)
                        .code(code)
                        .build())
                .meta(MetaData.builder().build())
                .timestamp(getCurrentTimestamp())
                .build();
    }

    private static Long getCurrentTimestamp() {
        return Instant.now().toEpochMilli();
    }

    public ZonedDateTime getTimestampAsDateTime() {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorDetails {
        private String message;
        private String code;
        private String field;
        private Object details;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetaData {
        @Builder.Default
        private String version = "1.0";
        private String requestId;
        private Object additional;
    }
}