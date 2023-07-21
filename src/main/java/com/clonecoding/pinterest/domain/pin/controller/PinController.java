package com.clonecoding.pinterest.domain.pin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pin")
public class PinController {

    // 모든 pin 조회
    @GetMapping("/all")
    public List<String> getAllPin(){
        return null;
    }

    // 유저가 작성한 pin 조회
    @GetMapping("/user")
    public List<String> getUserPin(){
        return null;
    }

    // pin 작성
    @PostMapping()
    public ResponseEntity<String> createPin(){
        return null;
    }

    // 특정 pin 조회
}
