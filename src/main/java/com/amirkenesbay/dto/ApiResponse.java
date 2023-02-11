package com.amirkenesbay.dto;

import com.amirkenesbay.enums.ApiStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private ApiStatus status;
    private int code;
    private Map<String, String> message;
    private T result;

    public static ApiResponse<String> getErrorResponse(int code, String message) {
        return new ApiResponse<>(ApiStatus.ERROR, code, Map.of("error", message), null);
    }
}
