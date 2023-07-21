package com.clonecoding.pinterest.user.controller;


import com.clonecoding.pinterest.user.dto.UserCreateRequestDto;
import com.clonecoding.pinterest.user.dto.UserCreateResponseDto;
import com.clonecoding.pinterest.user.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    @NonNull
    private UserService userService;

    @PostMapping("/signup")
    public UserCreateResponseDto signUpUser(@RequestBody UserCreateRequestDto requestDto) {
        return this.userService.signUpUser(requestDto);
    }

    @GetMapping("/test")
    public String test(){
        return "success";
    }
}
