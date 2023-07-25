package com.clonecoding.pinterest.global.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ResponseUtil responseUtil;

    @Autowired
    public MyAuthenticationEntryPoint(ResponseUtil responseUtil) {
        this.responseUtil = responseUtil;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        responseUtil.responseToExceptionResponseDto(response, HttpStatus.FORBIDDEN, "인증에 실패했습니다.");
    }
}
