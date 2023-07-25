package com.clonecoding.pinterest.global.security.jwt;

import com.clonecoding.pinterest.domain.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Cookie 에서 사용자 권한 값의 KEY
    public static final String CLAIM_AUTHORIZATION_KEY = "auth";
    public static final String CLAIM_EMAIL_KEY = "email";
    public static final String CLAIM_USERNAME_KEY = "username";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 200 *(60 * 60 * 1000L); // 200시간 (60*60*1000L=1시간)

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    public Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(User user) {
        Date date = new Date();
        return BEARER_PREFIX + Jwts.builder().setSubject(String.valueOf(user.getId())).claim(CLAIM_USERNAME_KEY, user.getUsername()).claim(CLAIM_EMAIL_KEY, user.getEmail()).claim(CLAIM_AUTHORIZATION_KEY, user.getRole()).setExpiration(new Date(date.getTime() + TOKEN_TIME)).setIssuedAt(date).signWith(key, signatureAlgorithm).compact();
    }

    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");
            ResponseCookie cookie = ResponseCookie.from(AUTHORIZATION_HEADER, token)
                    .path("/")
                    .httpOnly(true)
                    .maxAge(TOKEN_TIME / 1000)
                    .build();
            res.setHeader(cookie.getName(), cookie.getValue());

            //백엔드 API TEST용도
            Cookie cookieApi = new Cookie(AUTHORIZATION_HEADER, token);
            cookieApi.setPath("/");
            res.addCookie(cookieApi);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }

    public String subStringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(BEARER_PREFIX.length());
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    public String validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return "success";
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
//            return "pass";
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return "fail";
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        log.info("getTokenFromRequest");

        String token = request.getHeader("authorization");
        if (token != null) {
//            log.info(token);
            try {
                return URLDecoder.decode(token, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        //API TEST용
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {
            log.info("get cookie null!!");
        }

        return null;
    }
}

