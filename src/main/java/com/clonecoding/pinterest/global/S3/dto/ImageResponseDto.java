package com.clonecoding.pinterest.global.S3.dto;

import lombok.Getter;

@Getter
public class ImageResponseDto {

    private final Long imageId;
    private final String pinImageUrl;
    private final String title;
    private final String content;
    private final String username;

    private ImageResponseDto(Long imageId, String pinImageUrl, String title, String content, String username) {
        this.imageId = imageId;
        this.pinImageUrl = pinImageUrl;
        this.title = title;
        this.content = content;
        this.username = username;
    }
}
