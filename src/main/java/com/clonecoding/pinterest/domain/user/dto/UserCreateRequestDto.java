package com.clonecoding.pinterest.domain.user.dto;

import com.clonecoding.pinterest.domain.user.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDto {
    @Email
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{4,10}$")
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String password;
    private UserRoleEnum role = UserRoleEnum.USER;
    private String adminToken = "";
}
