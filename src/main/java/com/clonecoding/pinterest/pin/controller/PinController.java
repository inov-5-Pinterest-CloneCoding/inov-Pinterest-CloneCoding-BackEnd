package com.clonecoding.pinterest.pin.controller;

import com.clonecoding.pinterest.pin.dto.PinResponseDTO;
import com.clonecoding.pinterest.pin.service.PinService;
import com.clonecoding.pinterest.user.entity.User;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pin")
public class PinController {


    // 모든 pin 조회
    @GetMapping("/all")
    public ResponseEntity<List<PinResponseDTO>> getAllPin(){
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
    public ResponseEntity<String> getcheckPin() {return null;}
}
