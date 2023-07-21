package com.clonecoding.pinterest.global.exception.controller;

import com.clonecoding.pinterest.global.exception.dto.ExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "GlobalExceptionHandler")
public class GlobalExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponseDto> illegalArgumentExceptionHandler(IllegalArgumentException e){
        ExceptionResponseDto responseDto = new ExceptionResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponseDto> nullPointerExceptionHandler(IllegalArgumentException e){
        ExceptionResponseDto responseDto = new ExceptionResponseDto(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
}
