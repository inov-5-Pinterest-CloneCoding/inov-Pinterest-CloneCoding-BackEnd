package com.clonecoding.pinterest.domain.comment.repository;

import com.clonecoding.pinterest.domain.comment.entity.Comment;
import com.clonecoding.pinterest.domain.comment.entity.CommentLike;
import com.clonecoding.pinterest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
}
