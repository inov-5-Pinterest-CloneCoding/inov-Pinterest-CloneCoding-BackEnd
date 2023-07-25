package com.clonecoding.pinterest.domain.pin.dto;

import com.clonecoding.pinterest.domain.pin.entity.Pin;
import lombok.Getter;

@Getter
public class PinResponseDTO {

    private Long id;
    private String pinImageUrl;

    public PinResponseDTO(Pin pin){
        this.id = pin.getId();
        this.pinImageUrl = pin.getPinImageUrl();
    }
}
