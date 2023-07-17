package com.example.productservice.business.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private LocalDateTime localdatetime;
    private String message;
    private String details;

}