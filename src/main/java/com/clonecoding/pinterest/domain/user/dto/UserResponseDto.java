package com.clonecoding.pinterest.domain.user.dto;

import com.clonecoding.pinterest.domain.user.entity.UserRoleEnum;
import com.clonecoding.pinterest.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String email;
    private String username;
    private UserRoleEnum role;

    public UserResponseDto(User user){
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.role =user.getRole();
    }
}
