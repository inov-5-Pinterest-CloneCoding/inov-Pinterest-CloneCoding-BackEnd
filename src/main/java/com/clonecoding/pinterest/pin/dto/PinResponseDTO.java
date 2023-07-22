package com.clonecoding.pinterest.pin.dto;

import com.clonecoding.pinterest.pin.entity.Pin;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PinResponseDTO {
    private Long id;
    private String pinImageUrl;
//    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PinResponseDTO(Pin pin) {
        this.id = pin.getId();
        this.pinImageUrl = pin.getPinImageUrl();
//        this.title = pin.getTitle();
        this.createdAt = pin.getCreatedAt();
        this.modifiedAt = pin.getModifiedAt();
    }
}
