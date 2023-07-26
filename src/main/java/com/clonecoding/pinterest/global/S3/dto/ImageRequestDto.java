package com.clonecoding.pinterest.global.S3.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Schema(description = "이미지 등록 요청 DTO")
public class ImageRequestDto {
    private final String title;
    private final String content;
    private MultipartFile imageFile;

    public ImageRequestDto(String title, String content, MultipartFile imageFile) {
        this.title = title;
        this.content = content;
        this.imageFile = imageFile;
    }
}
