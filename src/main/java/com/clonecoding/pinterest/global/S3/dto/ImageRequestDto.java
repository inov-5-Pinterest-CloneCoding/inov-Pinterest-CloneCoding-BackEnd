package com.clonecoding.pinterest.global.S3.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "이미지 요청 DTO")
public class ImageRequestDto {
    private final String title;
    private final String content;
    private final String username;

    public ImageRequestDto(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
    }
}
