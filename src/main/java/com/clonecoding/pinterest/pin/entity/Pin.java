package com.clonecoding.pinterest.pin.entity;

import com.clonecoding.pinterest.entity.TimeStamped;
import com.clonecoding.pinterest.pin.dto.PinRequestDTO;
import com.clonecoding.pinterest.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pin extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pinImageUrl;

//    @Column(nullable = true)
//    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "pin", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("id DESC ")
    private List<Comment> commentList = new ArrayList<>();


    public Pin(PinRequestDTO requestDto, User user) {
        this.pinImageUrl = requestDto.getPinImageUrl();
        this.user = user;
//        this.title = title;
    }

    public void update(PinRequestDTO requestDto){
        pinImageUrl = requestDto.getPinImageUrl();
    }
}
