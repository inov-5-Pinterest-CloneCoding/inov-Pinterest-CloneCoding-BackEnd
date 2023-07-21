package com.clonecoding.pinterest.security.exception;

import com.clonecoding.pinterest.exception.dto.ExceptionResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ExceptionResponseDto responseDto = new ExceptionResponseDto(HttpStatus.FORBIDDEN, "인가 실패");
        String responseString = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(responseString);
    }
}
