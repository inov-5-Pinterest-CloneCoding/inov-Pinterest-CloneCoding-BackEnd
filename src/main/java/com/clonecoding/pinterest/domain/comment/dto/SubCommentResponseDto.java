package com.clonecoding.pinterest.domain.comment.dto;

import com.clonecoding.pinterest.domain.comment.entity.SubComment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubCommentResponseDto {
    private Long subcomment_id;
    private String subcomment;
    private LocalDateTime createdAt;
    private Integer countSubCommentLike;
    private boolean isSubCommentLike;

    public SubCommentResponseDto(SubComment subcomment) {
        this.subcomment_id = subcomment.getId();
        this.subcomment = subcomment.getSubComment();
        this.createdAt = subcomment.getCreatedAt();
        this.countSubCommentLike = subcomment.getSubCommentLikeList().size();
    }
}
