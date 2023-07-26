package com.clonecoding.pinterest.global.security.filter;

import com.clonecoding.pinterest.global.security.config.WebSecurityConfig;
import com.clonecoding.pinterest.global.security.exception.ResponseUtil;
import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ResponseUtil responseUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtAuthorizationFilter(JwtUtil jwtUtil, ResponseUtil responseUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.responseUtil = responseUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        boolean loginPass = JwtAuthenticationFilter.loginUrl.equals(request.getServletPath());
        boolean permitPass = Arrays.stream(WebSecurityConfig.matchRouteArray)
                .anyMatch(str -> str.equals(request.getServletPath()));

        if (loginPass || permitPass || !StringUtils.hasText(tokenValue)) {
            filterChain.doFilter(request, response);
        } else if (StringUtils.hasText(tokenValue)) {
            log.info("처리 전 토큰 : " + tokenValue);
            tokenValue = jwtUtil.subStringToken(tokenValue);
            log.info("처리 후 토큰 : " + tokenValue);
            switch (jwtUtil.validateToken(tokenValue)) {
                case "fail" -> {
                    log.info("case fail");
                    responseUtil.responseToExceptionResponseDto(response, HttpStatus.FORBIDDEN, "토큰 유효성 검증에 실패했습니다.");
                }
                default -> {
                    Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
//                    log.info(info.toString());
                    try {
//                        log.info(info.getSubject());
                        setAuthentication((String) info.get(JwtUtil.CLAIM_EMAIL_KEY));
                    } catch (Exception e) {
                        response.setStatus(403);
                        log.error(e.getMessage());
                        return;
                    }
                    filterChain.doFilter(request, response);
                }
            }
        }
    }

    // 인증 처리
    private void setAuthentication(String userEmail) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userEmail);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String userEmail) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
