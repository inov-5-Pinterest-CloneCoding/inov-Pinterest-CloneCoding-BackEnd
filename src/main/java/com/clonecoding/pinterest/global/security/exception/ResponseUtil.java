package com.clonecoding.pinterest.global.security.exception;

import com.clonecoding.pinterest.global.exception.dto.ExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseUtil {
    public void responseToExceptionResponseDto(HttpServletResponse response, HttpStatus httpStatus, String msg) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(httpStatus.value());
        ExceptionResponseDto responseDto = new ExceptionResponseDto(httpStatus, msg);
        String responseString = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(responseString);
    }
}
