package com.management.kbbs.controller;

import com.management.kbbs.dto.*;
import com.management.kbbs.entity.Comment;
import com.management.kbbs.entity.User;
import com.management.kbbs.exception.GlobalExceptionHandler;
import com.management.kbbs.repository.CommentRepository;
import com.management.kbbs.repository.UserRepository;
import com.management.kbbs.service.CommentService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final UserRepository userRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // 新增評論
//    @PreAuthorize("hasRole('ROLE_MEMBER')")
//    @PostMapping("/member/add")
//    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentRequestDTO requestDTO) {
//        try {
//            CommentDTO createdComment = commentService.createComment(requestDTO);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null); // 或返回一個錯誤訊息物件
//        }
//    }

    // 新增評論 (in RabbitMQ)
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/member/add")
    public ResponseEntity<String> createComment(@RequestBody CommentRequestDTO requestDTO) {
        // 發送請求數據到 RabbitMQ
        requestDTO.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        rabbitTemplate.convertAndSend(exchangeName, routingKey, requestDTO);
        return ResponseEntity.ok("評論新增完成！");
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
    public ResponseEntity<List<CommentSearchByBookDTO>> getCommentsByBookId(@PathVariable Long bookId) {
        List<CommentSearchByBookDTO> comments = commentService.getCommentsByBookId(bookId);
        return ResponseEntity.ok(comments);
    }

    // 查詢特定使用者留下的評論
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/{userId}")
    public ResponseEntity<List<CommentSearchByUserDTO>> getCommentsByUserId(@PathVariable Long userId) {
        List<CommentSearchByUserDTO> comments = commentService.getCommentsByUserId(userId);
        return ResponseEntity.ok(comments);
    }

    // 查詢所有書籍的平均評分與評論數
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/ratings")
    public ResponseEntity<List<BookEvaluateDTO>> getBookRatings() {
        List<BookEvaluateDTO> ratings = commentService.getBookRatings();
        return ResponseEntity.ok(ratings);
    }

    // 讓使用者修改個人評論
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PatchMapping("/member/updatecomment/{id}")
    public ResponseEntity<?> updateUserComment(@PathVariable Long id, @RequestBody CommentUpdateDTO updateDTO) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 確認用戶是否與評論的用戶匹配
        User user = userRepository.findByName(username)
                      .orElseThrow(() -> new GlobalExceptionHandler.UserNoAuthException("You are not authorized to update this comment."));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        if(!user.getName().equals(comment.getUser().getName())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No Auth to update this comment.");
        }

        CommentDTO updatedComment = commentService.updateComment(id, updateDTO);
        return ResponseEntity.ok("評論已修改成以下內容：「" + updatedComment.getContent() + "」！");
    }

    // 列出簡易的個人評論清單
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @GetMapping("/member/simplified")
    public ResponseEntity<List<UserSimpleCommentDTO>> getSimplifiedComments() {
        List<UserSimpleCommentDTO> comments = commentService.getSimplifiedComments();
        return ResponseEntity.ok(comments);
    }
}
