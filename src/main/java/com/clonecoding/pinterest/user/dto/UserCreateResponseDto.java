package com.clonecoding.pinterest.user.dto;

import com.clonecoding.pinterest.user.entity.User;
import com.clonecoding.pinterest.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class UserCreateResponseDto {
    private String email;
    private String username;
    private UserRoleEnum role;

    public UserCreateResponseDto(User user){
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role =user.getRole();
    }
}
