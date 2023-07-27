package com.clonecoding.pinterest.domain.comment.service;

import com.clonecoding.pinterest.domain.comment.dto.*;
import com.clonecoding.pinterest.domain.comment.entity.Comment;
import com.clonecoding.pinterest.domain.comment.entity.CommentLike;
import com.clonecoding.pinterest.domain.comment.entity.SubComment;
import com.clonecoding.pinterest.domain.comment.entity.SubCommentLike;
import com.clonecoding.pinterest.domain.comment.repository.CommentLikeRepository;
import com.clonecoding.pinterest.domain.comment.repository.CommentRepository;
import com.clonecoding.pinterest.domain.comment.repository.SubCommentLikeRepository;
import com.clonecoding.pinterest.domain.comment.repository.SubCommentRepository;
import com.clonecoding.pinterest.domain.pin.entity.Pin;
import com.clonecoding.pinterest.domain.pin.repository.PinRepository;
import com.clonecoding.pinterest.domain.user.entity.User;
import com.clonecoding.pinterest.domain.user.entity.UserRoleEnum;
import com.clonecoding.pinterest.domain.user.repository.UserRepository;
import com.clonecoding.pinterest.global.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final SubCommentRepository subcommentRepository;
    private final SubCommentLikeRepository subCommentLikeRepository;
    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity<String> createComment(String tokenValue, Long pinId, CommentRequestDto requestDto) {
        // 해당 핀이 DB에 존재하는지 확인
        Pin targetPin = pinRepository.findById(pinId).orElseThrow(() ->
                new NullPointerException("핀을 찾을 수 없습니다."));

        User currentUser = checkTokenFindUser(tokenValue);

        // requestDto를 포함한 comment 저장에 필요한 값들 담아서 주기
        Comment comment = new Comment(requestDto, targetPin, currentUser);

        // DB 저장 넘겨주기
        Comment saveComment = commentRepository.save(comment);

        return ResponseEntity.ok("댓글 등록 완료");
    }

    @Transactional
    public ResponseEntity<String> updateComment(String tokenValue, Long commentId, CommentRequestDto requestDto) {
        // 댓글 저장유무 확인
        Comment comment = findComment(commentId);

        User currentUser = checkTokenFindUser(tokenValue);

        // 권한 확인
        checkAuthority(comment, currentUser);

        // 수정
        comment.update(requestDto);

        return ResponseEntity.ok("댓글 수정 완료");
    }

    public ResponseEntity<String> deleteComment(String tokenValue, Long commentId) {
        // 댓글 저장유무 확인
        Comment comment = findComment(commentId);

        User currentUser = checkTokenFindUser(tokenValue);

        // 권한 확인
        checkAuthority(comment, currentUser);

        // 삭제
        commentRepository.delete(comment);

        return ResponseEntity.ok("댓글 삭제 완료");
    }

    private static final Comparator<CommentResponseDto> COMMENT_COMPARATOR =
            Comparator.comparing(CommentResponseDto::getCreatedAt);

    // 핀에 있는 댓글 목록을 반환하는 API
    public List<CommentResponseDto> getCommentList(String tokenValue, Long pinId) {
        Long userId = checkTokenFindUser(tokenValue).getId();

        Optional<Pin> pinOptional = pinRepository.findById(pinId);

        if (pinOptional.isPresent()) {
            Pin pin = pinOptional.get();
            return pin.getCommentList().stream()
                    .map(comment -> {
                        CommentResponseDto dto = new CommentResponseDto(comment);
                        dto.setCommentLike(comment.getCommentLikeList().stream().anyMatch(commentLike -> commentLike.getUser().getId().equals(userId)));
                        return dto;
                    })
                    .sorted(COMMENT_COMPARATOR)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public ResponseEntity<String> likeComment(String tokenValue , Long commentId) {

        User currentUser = checkTokenFindUser(tokenValue);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 댓글입니다."));

        CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, currentUser).orElse(null);

        if (commentLike == null) {
            CommentLike newCommentLike = new CommentLike(currentUser, comment);
            commentLikeRepository.save(newCommentLike);
            return ResponseEntity.ok("댓글 좋아요 성공");
        } else {
            commentLikeRepository.delete(commentLike);
            return ResponseEntity.ok("댓글 좋아요 취소");
        }
    }

    public ResponseEntity<String> createSubComment(String tokenValue, Long commentId, SubCommentRequestDto requestDto) {
        // 해당 댓글이 DB에 존재하는지 확인
        Comment targetComment = commentRepository.findById(commentId).orElseThrow(() ->
                new NullPointerException("댓글을 찾을 수 없습니다."));

        User currentUser = checkTokenFindUser(tokenValue);

        // requestDto를 포함한 subcomment 저장에 필요한 값들 담아서 주기
        SubComment subComment = new SubComment(requestDto, targetComment, currentUser);

        // DB 저장 넘겨주기
        SubComment saveSubComment = subcommentRepository.save(subComment);

        return ResponseEntity.ok("대댓글 등록 완료");
    }

    @Transactional
    public ResponseEntity<String> updateSubComment(String tokenValue, Long subCommentId, SubCommentRequestDto requestDto) {
        // 대댓글 저장유무 확인
        SubComment subComment = findSubComment(subCommentId);

        User currentUser = checkTokenFindUser(tokenValue);

        // 권한 확인
        checkAuthority(subComment, currentUser);

        // 수정
        subComment.update(requestDto);

        return ResponseEntity.ok("대댓글 수정 완료");
    }

    public ResponseEntity<String> deleteSubComment(String tokenValue, Long subCommentId) {
        // 대댓글 저장유무 확인
        SubComment subComment = findSubComment(subCommentId);

        User currentUser = checkTokenFindUser(tokenValue);

        // 권한 확인
        checkAuthority(subComment, currentUser);

        // 삭제
        subcommentRepository.delete(subComment);

        return ResponseEntity.ok("대댓글 삭제 완료");
    }

    private static final Comparator<SubCommentResponseDto> SUBCOMMENT_COMPARATOR =
            Comparator.comparing(SubCommentResponseDto::getCreatedAt);

    // 댓글에 있는 대댓글 목록을 반환하는 API
    public List<SubCommentResponseDto> getSubCommentList(String tokenValue, Long commentId) {
        Long userId = checkTokenFindUser(tokenValue).getId();

        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            return comment.getSubCommentList().stream()
                    .map(subComment -> {
                        SubCommentResponseDto dto = new SubCommentResponseDto(subComment);
                        dto.setSubCommentLike(subComment.getSubCommentLikeList().stream().anyMatch(subCommentLike -> subCommentLike.getUser().getId().equals(userId)));
                        return dto;
                    })
                    .sorted(SUBCOMMENT_COMPARATOR)
                    .toList();
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public ResponseEntity<String> likeSubComment(String tokenValue ,Long subCommentId) {

        User currentUser = checkTokenFindUser(tokenValue);

        SubComment subComment = subcommentRepository.findById(subCommentId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 대댓글입니다."));

        SubCommentLike subCommentLike = subCommentLikeRepository.findBySubCommentAndUser(subComment, currentUser).orElse(null);

        if (subCommentLike == null) {
            SubCommentLike newSubCommentLike = new SubCommentLike(currentUser, subComment);
            subCommentLikeRepository.save(newSubCommentLike);
            return ResponseEntity.ok("대댓글 좋아요 성공");
        } else {
            subCommentLikeRepository.delete(subCommentLike);
            return ResponseEntity.ok("대댓글 좋아요 취소");
        }
    }

    // Comment 조회 메서드
    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new NullPointerException("존재하지 않는 댓글 입니다.")
        );
    }

    // SubComment 조회 메서드
    private SubComment findSubComment(Long id) {
        return subcommentRepository.findById(id).orElseThrow(() ->
                new NullPointerException("존재하지 않는 대댓글 입니다.")
        );
    }

    // 토큰 유효성 검증 후 유저 찾아서 반환
    private User checkTokenFindUser(String tokenValue) {
        // 토큰 자르기
        String token = jwtUtil.subStringToken(tokenValue);

        // 토큰 검증
        if (!jwtUtil.validateToken(token).equals("success")) {
            throw new IllegalArgumentException("Token Error");
        }

        // 토큰에서 유저 id 가져와서 user 정보 조회
        Claims info = jwtUtil.getUserInfoFromToken(token);

        User user = userRepository.findById(Long.parseLong(info.getSubject())).orElseThrow(() ->
                new NullPointerException("존재하지 않는 회원입니다.")
        );

        return user;
    }

    // 수정, 삭제시 권한을 확인
    public void checkAuthority(Comment comment, User user) {
        // 사용자 확인
        boolean isSameUser = comment.getUser().getId() == user.getId();

        // 유저 권한 확인
        boolean isAdmin = user.getRole().getAuthority().equals(UserRoleEnum.ADMIN.getAuthority());

        // 사용자와 작성자가 일치하지 않거나 관리자가 아닌 경우 예외 발생
        if (!isSameUser && !isAdmin) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    public void checkAuthority(SubComment subcomment, User user) {
        // 사용자 확인
        boolean isSameUser = subcomment.getUser().getId() == user.getId();

        // 유저 권한 확인
        boolean isAdmin = user.getRole().getAuthority().equals(UserRoleEnum.ADMIN.getAuthority());

        // 사용자와 작성자가 일치하지 않거나 관리자가 아닌 경우 예외 발생
        if (!isSameUser && !isAdmin) {
            throw new IllegalArgumentException("작성자만 삭제/수정할 수 있습니다.");
        }
    }
}