package com.clonecoding.pinterest.domain.pin.dto;

import com.clonecoding.pinterest.domain.comment.dto.CommentResponseDto;
import com.clonecoding.pinterest.domain.pin.entity.Pin;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
public class PinResponseDTO {

    private Long id;
    private String pinImageUrl;
    private Integer countPinLike;
    private List<CommentResponseDto> commentList;
    private boolean isPinLike;

    public PinResponseDTO(Pin pin){
        this.id = pin.getId();
        this.pinImageUrl = pin.getPinImageUrl();
        this.commentList = pin.getCommentList().stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt))
                .toList();
        this.countPinLike = pin.getPinLikeList().size();
    }

    public PinResponseDTO(Pin pin, boolean isPinLike){
        this.id = pin.getId();
        this.pinImageUrl = pin.getPinImageUrl();
        this.commentList = pin.getCommentList().stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt))
                .toList();
        this.countPinLike = pin.getPinLikeList().size();
        this.isPinLike = isPinLike;
    }
}
