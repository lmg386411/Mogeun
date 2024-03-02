package com.mogun.backend.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ServiceStatus<T> {

    private int status;
    private String message;

    private T data;

    public static <T> ServiceStatus<T> errorStatus(String res) {

        return ServiceStatus.<T>builder()
                .status(200)
                .message(res)
                .build();
    }

    public static <T> ServiceStatus<T> okStatus() {

        return ServiceStatus.<T>builder()
                .status(100)
                .message("SUCCESS")
                .build();
    }
}
