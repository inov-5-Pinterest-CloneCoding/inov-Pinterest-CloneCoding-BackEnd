package com.clonecoding.pinterest.domain.user.service;

import com.clonecoding.pinterest.domain.user.dto.KakaoUserDto;
import com.clonecoding.pinterest.domain.user.dto.UserCreateRequestDto;
import com.clonecoding.pinterest.domain.user.dto.UserResponseDto;
import com.clonecoding.pinterest.domain.user.entity.UserRoleEnum;
import com.clonecoding.pinterest.domain.user.repository.UserRepository;
import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserService")
public class UserService {
    @NonNull
    private UserRepository userRepository;
    @NonNull
    private PasswordEncoder passwordEncoder;
    @NonNull
    private RestTemplate restTemplate;
    @NonNull
    private JwtUtil jwtUtil;

    @NonNull
    private IdenticonService identiconService;

    @Value("${client.url}")
    private String clientUrl;

    @Value("${kakao.login.callback.url}")
    private String kakaoCallbackUrl;

    @Value("${kakao.login.client.id}")
    private String kakaoLoginClientId;

    public UserResponseDto signUpUser(UserCreateRequestDto requestDto) {
        if (requestDto.getRole().getAuthority().equals(UserRoleEnum.ADMIN.getAuthority())) {
            // adminToken이 승인된 토큰이면 통과
            throw new IllegalArgumentException("승인되지 않은 ADMIN 입니다.");
        }
        Optional<User> user = userRepository.findByEmail(requestDto.getEmail());
        if (user.isPresent())
            throw new IllegalArgumentException("가입된 이메일 입니다.");

        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        String userEmail = requestDto.getEmail();
        User savedUser = User.builder()
                .email(userEmail)
                .profileImageUrl(identiconService.makeIdenticonUrl(userEmail))
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .role(requestDto.getRole())
                .build();

        userRepository.save(savedUser);

        UserResponseDto responseDto = new UserResponseDto(savedUser);
        return responseDto;


    }

    @Transactional
    public User kakaoSignUpOrLinkUser(String code) throws JsonProcessingException {
        String accessToken = getToken(code);
        log.info("카카오 서버에서 토큰 받기 성공적");
        KakaoUserDto kakaoUserDto = getKakaoUserInfo(accessToken);

        User user = userRepository.findByKakaoId(kakaoUserDto.getId()).orElse(null);
        if (user == null) user = registerKakaoUser(kakaoUserDto);
        return user;
    }

    private KakaoUserDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserDto(id, nickname, email);
    }

    private String getToken(String code) throws JsonProcessingException {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoLoginClientId);
        body.add("redirect_uri", clientUrl + kakaoCallbackUrl);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }
    @Transactional
    public User registerKakaoUser(KakaoUserDto kakaoUserDto) {
        User user = userRepository.findByEmail(kakaoUserDto.getEmail()).orElse(null);
        if(user != null) user.kakaoIdUpdate(kakaoUserDto);
        else {
            String randomPwd = passwordEncoder.encode(String.valueOf(kakaoUserDto.getId()));
            user = new User(kakaoUserDto, randomPwd);
            userRepository.save(user);
        }
        return user;
    }
}
