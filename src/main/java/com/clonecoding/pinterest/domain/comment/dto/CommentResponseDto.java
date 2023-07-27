package com.clonecoding.pinterest.domain.comment.dto;

import com.clonecoding.pinterest.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class CommentResponseDto {
    private Long comment_id;
    private String comment;
    private LocalDateTime createdAt;
    private List<SubCommentResponseDto> subCommentList;
    private Integer countCommentLike;
    private boolean isCommentLike;

    public CommentResponseDto(Comment comment) {
        this.comment_id = comment.getId();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.subCommentList = comment.getSubCommentList().stream()
                .map(SubCommentResponseDto::new)
                .sorted(Comparator.comparing(SubCommentResponseDto::getCreatedAt))
                .toList();
        this.countCommentLike = comment.getCommentLikeList().size();
    }
}
