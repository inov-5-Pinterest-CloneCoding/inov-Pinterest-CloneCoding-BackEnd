package com.clonecoding.pinterest.domain.pin.entity;

import com.clonecoding.pinterest.domain.comment.entity.Comment;
import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "pin", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "pin", cascade = CascadeType.ALL)
    private List<PinLike> pinLikeList = new ArrayList<>();

    @Builder
    public Pin(String pinImageUrl, User user) {
        this.pinImageUrl = pinImageUrl;
        this.user = user;
    }

    public void modifyPin(String pinImageUrl){
        this.pinImageUrl = pinImageUrl;
    }
}
