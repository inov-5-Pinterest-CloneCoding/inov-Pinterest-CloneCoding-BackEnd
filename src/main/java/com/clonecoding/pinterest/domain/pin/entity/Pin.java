package com.clonecoding.pinterest.domain.pin.entity;

import com.clonecoding.pinterest.domain.pin.dto.PinRequestDTO;
import com.clonecoding.pinterest.domain.pin.dto.PinResponseDTO;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import com.clonecoding.pinterest.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Pin extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pinImageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Pin(String pinImageUrl, User user) {
        this.pinImageUrl = pinImageUrl;
        this.user = user;
    }

    public void modifyPin(String pinImageUrl){
        this.pinImageUrl = pinImageUrl;
    }
}
