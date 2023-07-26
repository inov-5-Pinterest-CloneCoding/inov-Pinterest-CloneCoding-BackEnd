package com.clonecoding.pinterest.global.S3.dto;

import com.clonecoding.pinterest.domain.pin.entity.Pin;
import com.clonecoding.pinterest.global.S3.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "이미지 요청시 응답 DTO")
@Getter
public class ImageResponseDto {

    private final Long imageId;
    private final String pinImageUrl;
    private final String title;
    private final String content;
    private final String userEmail;

    public ImageResponseDto(Image image){
        this.imageId = image.getId();
        this.pinImageUrl = image.getPinImageUrl();
        this.title = image.getTitle();
        this.content = image.getContent();
        this.userEmail = image.getUser().getEmail();
    }
}
