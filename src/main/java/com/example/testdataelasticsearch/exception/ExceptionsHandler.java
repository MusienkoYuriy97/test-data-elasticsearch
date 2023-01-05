package com.example.testdataelasticsearch.exception;

import io.micrometer.common.util.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> badResponse(Exception e) {
        return ResponseEntity.badRequest()
                .body(StringUtils.isBlank(e.getMessage()) ? "Some bad things happened" : e.getMessage());
    }
}