package com.clonecoding.pinterest.global.security.filter;

import com.clonecoding.pinterest.global.exception.dto.ExceptionResponseDto;
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
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
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

    // config에서 AuthenticationEntryPoint handling 할거라면 처리X
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info(failed.getMessage());

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ExceptionResponseDto responseDto = new ExceptionResponseDto(HttpStatus.UNAUTHORIZED, failed.getMessage());

        //object to json string ( responseDto에 getter 빠지면 No serializer found ~ error )
        String responseString = new ObjectMapper().writeValueAsString(responseDto);
        response.getWriter().write(responseString);
    }
}
