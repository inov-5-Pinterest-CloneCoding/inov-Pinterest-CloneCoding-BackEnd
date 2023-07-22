package com.clonecoding.pinterest.domain.user.service;

import com.clonecoding.pinterest.domain.user.dto.UserCreateRequestDto;
import com.clonecoding.pinterest.domain.user.dto.UserCreateResponseDto;
import com.clonecoding.pinterest.domain.user.repository.UserRepository;
import com.clonecoding.pinterest.domain.user.entity.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @NonNull
    private UserRepository userRepository;
    @NonNull
    private PasswordEncoder passwordEncoder;

    public UserCreateResponseDto signUpUser(UserCreateRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
        if (user.isPresent())
            throw new IllegalArgumentException("가입된 이메일 입니다.");
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return new UserCreateResponseDto(this.userRepository.save(new User(requestDto)));
    }
}
