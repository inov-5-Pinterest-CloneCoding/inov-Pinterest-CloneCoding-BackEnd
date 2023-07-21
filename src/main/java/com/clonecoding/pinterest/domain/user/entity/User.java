package com.clonecoding.pinterest.domain.user.entity;

import com.clonecoding.pinterest.domain.user.dto.UserCreateRequestDto;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public User(UserCreateRequestDto requestDto){
        this.email = requestDto.getEmail();
        this.username = requestDto.getUsername();
        this.password = requestDto.getPassword();
        this.role = requestDto.getRole();
    }
}
