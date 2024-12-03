package com.management.kbbs.repository;

import com.management.kbbs.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 根據書籍 ID 查詢評論
    List<Comment> findByBookId(Long bookId);

    // 根據使用者 ID 查詢評論
    List<Comment> findByUserId(Long userId);
}