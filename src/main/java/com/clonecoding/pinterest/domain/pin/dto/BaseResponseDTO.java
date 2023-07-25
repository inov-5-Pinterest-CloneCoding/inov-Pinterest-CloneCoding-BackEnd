package com.clonecoding.pinterest.domain.pin.dto;

import lombok.Getter;

@Getter
public class BaseResponseDTO {

    private String msg;

    private int status;

    public BaseResponseDTO(String msg, int status) {
        this.msg = msg;
        this.status = status;
    }
}