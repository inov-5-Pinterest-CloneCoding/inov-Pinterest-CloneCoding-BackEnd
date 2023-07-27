package com.clonecoding.pinterest.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SubCommentRequestDto {
    @NotBlank
    private String subComment;
}
