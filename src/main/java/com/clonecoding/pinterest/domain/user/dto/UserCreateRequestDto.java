package com.clonecoding.pinterest.domain.user.dto;

import com.clonecoding.pinterest.domain.user.entity.UserRoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {
    private String email;
    private String username;
    private String password;
    private UserRoleEnum role = UserRoleEnum.USER;
    private String adminToken = "";
}
