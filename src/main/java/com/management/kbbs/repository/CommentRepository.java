package com.management.kbbs.repository;

import com.management.kbbs.dto.BookEvaluateDTO;
import com.management.kbbs.dto.UserSimpleCommentDTO;
import com.management.kbbs.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 根據書籍 ID 查詢評論
    List<Comment> findByBookId(Long bookId);

    // 根據使用者 ID 查詢評論
    List<Comment> findByUserId(Long userId);

    // 書籍的平均評分與評論數
    @Query("SELECT new com.management.kbbs.dto.BookEvaluateDTO(b.id, b.title, AVG(c.rating), COUNT(c)) " +
            "FROM Comment c " +
            "JOIN c.book b " +
            "GROUP BY b.id, b.title " +
            "ORDER BY AVG(c.rating) DESC ")
    List<BookEvaluateDTO> findAverageRatingAndCountByBook();

    // 列出簡易的個人評論清單
    @Query("SELECT new com.management.kbbs.dto.UserSimpleCommentDTO(c.id, c.book.title, c.book.author, c.content, c.rating) " +
            "FROM Comment c " +
            "JOIN c.book b " +
            "JOIN c.user u " +  // 確保正確使用 'user' 欄位
            "WHERE u.name = :username " +
            "ORDER BY c.id DESC ")
    List<UserSimpleCommentDTO> findAllCommentsWithBookInfo(@Param("username") String username);
}