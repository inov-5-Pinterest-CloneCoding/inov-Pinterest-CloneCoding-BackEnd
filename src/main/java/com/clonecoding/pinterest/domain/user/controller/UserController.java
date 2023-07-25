package com.clonecoding.pinterest.domain.user.controller;


import com.clonecoding.pinterest.domain.user.dto.UserCreateRequestDto;
import com.clonecoding.pinterest.domain.user.dto.UserLoginDto;
import com.clonecoding.pinterest.domain.user.dto.UserResponseDto;
import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.domain.user.service.UserService;
import com.clonecoding.pinterest.global.exception.validation.ValidationUtil;
import com.clonecoding.pinterest.global.security.filter.UserDetailsImpl;
import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j(topic = "UserController")
@RequestMapping("/api/user")
public class UserController {
    @NonNull
    private UserService userService;
    @NonNull
    private JwtUtil jwtUtil;
    @NonNull
    private ValidationUtil validationUtil;

    @Operation(summary = "ADMIN이라면 TOKEN값을 같이 보내주세요! 기본 유저라면 role과 adminToken은 안보내도 기본 USER로 생성됩니다.")
    @PostMapping("/signup")
    public UserResponseDto signUpUser(@RequestBody @Valid UserCreateRequestDto requestDto, BindingResult bindingResult){
        validationUtil.bindingResultHandle(bindingResult);
        return this.userService.signUpUser(requestDto);
    }

    @Operation(summary = "Security로 막혀있지 않아요 cors 확인용으로 쓸게요")
    @PostMapping("/cors")
    public String corsTest(@RequestBody UserLoginDto userLoginDto) {
        return "success";
    }

    @Operation(summary = "로컬 로그인 입니다.")
    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto loginDto) {
        //security 에서 로그인 라우트로 사용하기 때문에 안들어오는 컨트롤러 입니다. swagger 에 api 일치시키기 위해 만들어둘게요
        log.info("Security 에서 담당하는 로그인 라우트입니다.");
        return null;
    }

    @Operation(summary = "Security로 막혀있어요 token 잘 받는지 확인용을 쓸게요")
    @GetMapping("/token")
    public User tokenTest(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getUser();
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
