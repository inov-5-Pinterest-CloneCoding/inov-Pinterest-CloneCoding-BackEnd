package com.clonecoding.pinterest.global.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ExceptionResponseDto {
    private HttpStatus httpStatus;
    private String message;
}
