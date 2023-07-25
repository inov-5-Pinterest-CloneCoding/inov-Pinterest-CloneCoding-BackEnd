package com.clonecoding.pinterest.domain.pin.controller;

import com.clonecoding.pinterest.domain.pin.dto.BaseResponseDTO;
import com.clonecoding.pinterest.domain.pin.dto.PinRequestDTO;
import com.clonecoding.pinterest.domain.pin.dto.PinResponseDTO;
import com.clonecoding.pinterest.domain.pin.service.PinService;
import com.clonecoding.pinterest.global.security.filter.UserDetailsImpl;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pin")
public class PinController {

    private final PinService pinService;

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
    // 특정 pin 조회



    // pin 작성
    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PinResponseDTO> createPin(
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        PinRequestDTO requestDTO = new PinRequestDTO(imageFile);
        PinResponseDTO response = pinService.createPin(userDetails.getUser().getId(),requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // pin 수정
    @PutMapping("/{pinId}")
    public ResponseEntity<PinResponseDTO> modifyPin(@PathVariable("pinId") Long pinId,
                                                    @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){

        PinRequestDTO requestDTO = new PinRequestDTO(imageFile);
        PinResponseDTO responseDTO = pinService.modifyPin(pinId, requestDTO,userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    // 삭제
    @DeleteMapping("/{pinId}")
    public ResponseEntity<BaseResponseDTO> deletePin(@PathVariable("pinId") Long pinId
                                                    ,@AuthenticationPrincipal UserDetailsImpl userDetails){
        BaseResponseDTO responseDTO = pinService.deletePin(pinId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
