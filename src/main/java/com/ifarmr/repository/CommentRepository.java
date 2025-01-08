package com.ifarmr.repository;

import com.ifarmr.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {
}
