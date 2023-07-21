package com.clonecoding.pinterest.user.dto;

import com.clonecoding.pinterest.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {
    private String email;
    private String username;
    private String password;
    private UserRoleEnum role = UserRoleEnum.USER;
}