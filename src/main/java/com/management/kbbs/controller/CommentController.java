package com.management.kbbs.controller;

import com.management.kbbs.dto.CommentDTO;
import com.management.kbbs.service.CommentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 新增評論
    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return ResponseEntity.ok(createdComment);
    }

    // 透過 ID 查詢評論
    @GetMapping("/{id}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long id) {
        CommentDTO comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    // 更新評論
    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        CommentDTO updatedComment = commentService.updateComment(id, commentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 刪除評論
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    // 查詢所有評論
    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAllComments() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // 查詢特定書籍獲得的評論
    @GetMapping("/books/{bookId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByBookId(@PathVariable Long bookId) {
        List<CommentDTO> comments = commentService.getCommentsByBookId(bookId);
        return ResponseEntity.ok(comments);
    }

    // 查詢特定使用者留下的評論
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByUserId(@PathVariable Long userId) {
        List<CommentDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }
}
