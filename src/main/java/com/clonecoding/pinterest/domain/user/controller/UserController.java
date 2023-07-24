package com.clonecoding.pinterest.domain.user.controller;


import com.clonecoding.pinterest.domain.user.dto.UserCreateRequestDto;
import com.clonecoding.pinterest.domain.user.dto.UserLoginDto;
import com.clonecoding.pinterest.domain.user.dto.UserResponseDto;
import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.domain.user.service.UserService;
import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    @NonNull
    private UserService userService;
    @NonNull
    private JwtUtil jwtUtil;

    @Operation(summary = "ADMIN이라면 TOKEN값을 같이 보내주세요! 기본 유저라면 role과 adminToken은 안보내도 기본 USER로 생성됩니다.")
    @PostMapping("/signup")
    public UserResponseDto signUpUser(@RequestBody UserCreateRequestDto requestDto) {
        return this.userService.signUpUser(requestDto);
    }

    @Operation(summary = "Security로 막혀있지 않아요 cors 확인용으로 쓸게요")
    @PostMapping("/cors")
    public String corsTest(@RequestBody UserLoginDto userLoginDto) {
        return "success";
    }

    @Operation(summary = "아직 토큰 요청에 사용하지 않은 인가코드라면 확인할 수 있어요")
    @PostMapping("/kakao/login")
    @Transactional
    public UserResponseDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        User user = userService.kakaoSignUpOrLinkUser(code);
        jwtUtil.addJwtToCookie(jwtUtil.createToken(user), response);
        return new UserResponseDto(user);
    }
}
