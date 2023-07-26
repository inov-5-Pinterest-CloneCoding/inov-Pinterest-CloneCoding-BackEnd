package com.clonecoding.pinterest.global.security.filter;

import com.clonecoding.pinterest.global.exception.dto.ExceptionResponseDto;
import com.clonecoding.pinterest.global.security.exception.ResponseUtil;
import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import com.clonecoding.pinterest.domain.user.dto.UserLoginDto;
import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.domain.user.entity.UserRoleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public static final String loginUrl = "/api/user/login";
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil, ResponseUtil responseUtil) {
        this.jwtUtil = jwtUtil;
        this.responseUtil = responseUtil;
        setFilterProcessesUrl(JwtAuthenticationFilter.loginUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            UserLoginDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserLoginDto.class);
            System.out.println(requestDto);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        log.info("로그인 성공 및 JWT 생성");
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(user);
        jwtUtil.addJwtToCookie(token, response);
    }

    //authenticationEntryPoint는 기본적으로 모든 authenticated 거절을 핸들링한다.
    //unsuccessfulAuthentication은 UsernamePasswordAuthenticationFilter에 한해서만 핸들링
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info(failed.getMessage());
        responseUtil.responseToExceptionResponseDto(response, HttpStatus.UNAUTHORIZED, failed.getMessage());
    }
}
