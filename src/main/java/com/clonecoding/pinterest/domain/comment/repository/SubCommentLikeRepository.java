package com.clonecoding.pinterest.domain.comment.repository;

import com.clonecoding.pinterest.domain.comment.entity.SubComment;
import com.clonecoding.pinterest.domain.comment.entity.SubCommentLike;
import com.clonecoding.pinterest.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubCommentLikeRepository extends JpaRepository<SubCommentLike, Long> {
    Optional<SubCommentLike> findBySubCommentAndUser(SubComment subComment, User user);
}
