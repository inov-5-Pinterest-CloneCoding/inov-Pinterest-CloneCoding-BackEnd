package com.clonecoding.pinterest.global.security.filter;

import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getTokenFromRequest(request);
        log.info("first : " + tokenValue);
        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            tokenValue = jwtUtil.subStringToken(tokenValue);
            log.info(tokenValue);
            switch (jwtUtil.validateToken(tokenValue)) {
                case "fail" -> {
                    log.info("case fail");
                    response.setStatus(403);
                    //void 여서 return 없어도 된다. if 다음 동작도 없으므로 마지막
                }
                case "pass" -> {
                    log.info("case pass");
                    filterChain.doFilter(request, response);
                }
                default -> {
                    Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                    System.out.println(info);
                    try {
                        System.out.println(info.getSubject());
                        setAuthentication((String) info.get(JwtUtil.CLAIM_EMAIL_KEY));
                    } catch (Exception e) {
                        response.setStatus(403);
                        log.error(e.getMessage());
                        return;
                    }
                    filterChain.doFilter(request, response);
                }
            }
        } else filterChain.doFilter(request, response);
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
