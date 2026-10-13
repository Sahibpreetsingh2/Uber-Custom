package com.sahib.uberApp.UberApplication.advises;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {

    @JsonFormat(pattern = "dd:MM:yyyy hh:mm:ss")
    private LocalDateTime localDateTime;
    private T data;
    private ApiError apiError;

    public ApiResponse(){
        localDateTime = LocalDateTime.now();
    }

    public ApiResponse(T data){
        this();
        this.data = data;
    }

    public ApiResponse(ApiError apiError){
        this();
        this.apiError = apiError;
    }

    public ApiResponse(LocalDateTime localDateTime, T data, ApiError apiError) {
        this.localDateTime = localDateTime;
        this.data = data;
        this.apiError = apiError;
    }
}

