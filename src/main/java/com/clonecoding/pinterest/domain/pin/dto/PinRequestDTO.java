package com.clonecoding.pinterest.domain.pin.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PinRequestDTO {
    private String pinImageUrl;
    private MultipartFile imageFile;

    public PinRequestDTO(MultipartFile imageFile){
        this.imageFile = imageFile;
    }

}
