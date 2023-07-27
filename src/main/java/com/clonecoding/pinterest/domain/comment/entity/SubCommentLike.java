package com.clonecoding.pinterest.domain.comment.entity;

import com.clonecoding.pinterest.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SubCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subComment_id")
    private SubComment subComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SubCommentLike(User user, SubComment subComment) {
        this.user = user;
        this.subComment = subComment;
    }
}
