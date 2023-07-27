package com.clonecoding.pinterest.domain.user.entity;

import com.clonecoding.pinterest.domain.user.dto.KakaoUserDto;
import com.clonecoding.pinterest.domain.user.dto.UserCreateRequestDto;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User (String email, String username, String password, UserRoleEnum role,String profileImageUrl){
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.profileImageUrl =  profileImageUrl;
    }


    public User(UserCreateRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.role = requestDto.getRole();
    }

    public User(KakaoUserDto kakaoUserDto, String password,String profileImageUrl){
        this.email = kakaoUserDto.getEmail();
        this.username = kakaoUserDto.getUsername();
        this.password = password;
        //카카오 유저는 기본 USER
        this.role = UserRoleEnum.USER;
        this.profileImageUrl = profileImageUrl;
    }

    public User kakaoIdUpdate(KakaoUserDto kakaoUserDto){
        this.kakaoId = kakaoUserDto.getId();
        return this;
    }
}
