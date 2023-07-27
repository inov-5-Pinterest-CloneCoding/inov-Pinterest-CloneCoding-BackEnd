package com.clonecoding.pinterest.domain.comment.controller;

import com.clonecoding.pinterest.domain.comment.dto.*;
import com.clonecoding.pinterest.domain.comment.service.CommentService;
import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pin/{pinId}/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성 API
    @PostMapping()
    public ResponseEntity<String> createComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                @PathVariable Long pinId,
                                                @RequestBody @Valid CommentRequestDto requestDto
    ) {
        return commentService.createComment(tokenValue, pinId, requestDto);
    }

    // 핀에 있는 댓글 목록을 반환하는 API
    @GetMapping()
    public List<CommentResponseDto> getCommentList(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                   @PathVariable Long pinId) {
        return commentService.getCommentList(tokenValue, pinId);
    }

    // 댓글 수정 API
    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                @PathVariable Long commentId,
                                                @RequestBody @Valid CommentRequestDto requestDto) {
        return commentService.updateComment(tokenValue, commentId, requestDto);
    }

    // 댓글 삭제 API
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                @PathVariable Long commentId) {
        return commentService.deleteComment(tokenValue, commentId);
    }

    // 댓글 좋아요 API
    @PostMapping("/{commentId}/like")
    public ResponseEntity<String> likeComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                              @PathVariable Long commentId) {
        return commentService.likeComment(tokenValue,commentId);
    }

    // 대댓글 작성 API
    @PostMapping("/{commentId}")
    public ResponseEntity<String> createSubComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                   @PathVariable Long commentId,
                                                   @RequestBody @Valid SubCommentRequestDto requestDto
    ) {
        return commentService.createSubComment(tokenValue, commentId, requestDto);
    }

    // 댓글에 있는 대댓글 목록을 반환하는 API
    @GetMapping("/{commentId}")
    public List<SubCommentResponseDto> getSubCommentList(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                         @PathVariable Long commentId) {
        return commentService.getSubCommentList(tokenValue, commentId);
    }

    // 대댓글 수정 API
    @PutMapping("/{commentId}/{subCommentId}")
    public ResponseEntity<String> updateSubComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                   @PathVariable Long subCommentId,
                                                   @RequestBody @Valid SubCommentRequestDto requestDto) {
        return commentService.updateSubComment(tokenValue, subCommentId, requestDto);
    }

    // 대댓글 삭제 API
    @DeleteMapping("/{commentId}/{subCommentId}")
    public ResponseEntity<String> deleteSubComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                   @PathVariable Long subCommentId) {
        return commentService.deleteSubComment(tokenValue, subCommentId);
    }

    // 대댓글 좋아요 API
    @PostMapping("/{commentId}/{subCommentId}/like")
    public ResponseEntity<String> likeSubComment(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue,
                                                 @PathVariable Long subCommentId) {
        return commentService.likeSubComment(tokenValue,subCommentId);
    }
}
