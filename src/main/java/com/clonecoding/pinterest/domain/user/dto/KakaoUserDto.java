package com.clonecoding.pinterest.domain.user.dto;

import lombok.Getter;

@Getter
public class KakaoUserDto {
    private Long id;
    private String email;
    private String username;

    public KakaoUserDto(Long id, String nickname, String email) {
        this.id = id;
        this.email = email;
        this.username = nickname;
    }
}