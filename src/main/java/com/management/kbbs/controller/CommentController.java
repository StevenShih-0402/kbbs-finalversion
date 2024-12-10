package com.management.kbbs.controller;

import com.management.kbbs.dto.*;
import com.management.kbbs.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 新增評論
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/member/add")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentRequestDTO requestDTO) {
        try {
            CommentDTO createdComment = commentService.createComment(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 或返回一個錯誤訊息物件
        }
    }

    // 透過 ID 查詢評論
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    // 更新評論
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/admin/update/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentUpdateDTO updateDTO) {
        CommentDTO updatedComment = commentService.updateComment(id, updateDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 刪除評論
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    // 查詢所有評論
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // 查詢特定書籍獲得的評論
    @GetMapping("/public/books/{bookId}")
    public ResponseEntity<List<CommentSearchDTO>> getCommentsByBookId(@PathVariable Long bookId) {
        List<CommentSearchDTO> comments = commentService.getCommentsByBookId(bookId);
        return ResponseEntity.ok(comments);
    }

    // 查詢特定使用者留下的評論
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/{userId}")
    public ResponseEntity<List<CommentSearchDTO>> getCommentsByUserId(@PathVariable Long userId) {
        List<CommentSearchDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    // 查詢所有書籍的平均評分與評論數
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/ratings")
    public ResponseEntity<List<BookEvaluateDTO>> getBookRatings() {
        List<BookEvaluateDTO> ratings = commentService.getBookRatings();
        return ResponseEntity.ok(ratings);
    }
}
