package com.clonecoding.pinterest.domain.comment.entity;

import com.clonecoding.pinterest.domain.comment.dto.CommentRequestDto;
import com.clonecoding.pinterest.domain.pin.entity.Pin;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import com.clonecoding.pinterest.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "pin_id")
    private Pin pin;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<SubComment> subCommentList;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentLike> commentLikeList = new ArrayList<>();

    public Comment(CommentRequestDto requestDto, Pin pin, User user) {
        this.comment = requestDto.getComment();
        this.user = user;
        this.pin = pin;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
