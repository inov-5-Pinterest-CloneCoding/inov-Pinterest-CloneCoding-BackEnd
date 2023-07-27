package com.clonecoding.pinterest.domain.comment.entity;

import com.clonecoding.pinterest.domain.comment.dto.SubCommentRequestDto;
import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class SubComment extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subComment;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "subComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubCommentLike> subCommentLikeList = new ArrayList<>();

    public SubComment(SubCommentRequestDto requestDto, Comment comment, User user) {
        this.subComment= requestDto.getSubComment();
        this.comment = comment;
        this.user = user;
    }

    public void update(SubCommentRequestDto requestDto) {
        this.subComment = requestDto.getSubComment();
    }
}
