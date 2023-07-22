package com.clonecoding.pinterest.pin.dto;

import com.clonecoding.pinterest.pin.entity.Pin;

public class PinDTO {

    private Long id;

//    private String title;
    private String pinImageUrl;

    public PinDTO(Pin pin) {
        this.id = pin.getId();
//        this.title = pin.getTitle();
        this.pinImageUrl = pin.getPinImageUrl();
    }




}
