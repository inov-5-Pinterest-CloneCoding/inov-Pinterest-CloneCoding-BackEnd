package com.clonecoding.pinterest.domain.comment.repository;

import com.clonecoding.pinterest.domain.comment.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
}
