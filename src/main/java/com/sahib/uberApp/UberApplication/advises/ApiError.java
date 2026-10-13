package com.sahib.uberApp.UberApplication.advises;

import lombok.AllArgsConstructor;
//import lombok.Builder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiError {

    private LocalDateTime localDateTime;
    private String message;
    private HttpStatus httpStatus;
    List<String> errors;



}
