package com.clonecoding.pinterest.global.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
    private final ResponseUtil responseUtil;

    @Autowired
    public MyAccessDeniedHandler(ResponseUtil responseUtil) {
        this.responseUtil = responseUtil;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        responseUtil.responseToExceptionResponseDto(response, HttpStatus.FORBIDDEN, "인가 실패");
    }
}
