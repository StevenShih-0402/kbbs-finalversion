package com.management.kbbs.service;

import com.management.kbbs.dto.*;
import com.management.kbbs.entity.Book;
import com.management.kbbs.entity.Comment;
import com.management.kbbs.entity.User;
import com.management.kbbs.repository.BookRepository;
import com.management.kbbs.repository.CommentRepository;

import com.management.kbbs.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // 新增一條評論
//    public CommentDTO createComment(CommentRequestDTO requestDTO) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        User user = userRepository.findByName(username)
//                                  .orElseThrow(() -> new RuntimeException("User not found with User: " + username));
//        Book book = bookRepository.findById(requestDTO.getBookId())
//                                  .orElseThrow(() -> new RuntimeException("Book not found with ID: " + requestDTO.getBookId()));
//
//        Comment savedComment = commentRepository.save(setNewComment(user, book, requestDTO));
//
//        // 發送事件到 Kafka
//        // 非同步發送 Kafka 消息，快速返回
//        CompletableFuture.runAsync(() -> sendCommentEvent(user, book, requestDTO));
//
//        return convertToDTO(savedComment);
//    }

    // 新增一條評論 (in RabbitMQ)
    @RabbitListener(queues = "${rabbitmq.queue.name}")
    @Transactional
    public void handleCommentRequest(CommentRequestDTO requestDTO) {
        User user = userRepository.findByName(requestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with User: " + requestDTO.getUsername()));
        Book book = bookRepository.findById(requestDTO.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + requestDTO.getBookId()));

        Comment savedComment = commentRepository.save(setNewComment(user, book, requestDTO));
        // 可以根據需求進一步處理 savedComment，比如記錄日誌或發送通知
    }

    // 查詢特定的評論
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));
        return convertToDTO(comment);
    }

    // 更新評論
    public CommentDTO updateComment(Long id, CommentUpdateDTO updateDTO) {
        Comment existingComment = commentRepository.findById(id)
                                                   .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        editComment(existingComment, updateDTO);

        Comment updatedComment = commentRepository.save(existingComment);
        return convertToDTO(updatedComment);
    }

    // 刪除一條評論
    @Transactional
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found with ID: " + id);
        }
        commentRepository.deleteById(id);
    }

    // 查詢所有評論
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .collect(Collectors.toList());
    }

    // 查詢特定書籍獲得的評論
    public List<CommentSearchByBookDTO> getCommentsByBookId(Long bookId) {
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream()
                       .map(this::convertToSearchBookDTO)
                       .collect(Collectors.toList());
    }


    // 查詢特定用戶留下的評論
    public List<CommentSearchByUserDTO> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                       .map(this::convertToSearchUserDTO)
                       .collect(Collectors.toList());
    }

    // 查詢書籍的平均評分與評論數
    public List<BookEvaluateDTO> getBookRatings() {
        return commentRepository.findAverageRatingAndCountByBook();
    }

    // 讓用戶查詢個人的簡易的評論資料
    public List<UserSimpleCommentDTO> getSimplifiedComments() {
        // 獲取當前登入使用者的名稱
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return commentRepository.findAllCommentsWithBookInfo(username);
    }





    // 將 Comment 轉換成 CommentDTO
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setRating(comment.getRating());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUser(comment.getUser());
        commentDTO.setBook(comment.getBook());

        return commentDTO;
    }

    // 新增評論的資料轉換
    private Comment setNewComment(User user, Book book, CommentRequestDTO requestDTO){
        Comment comment = new Comment();
        comment.setContent(requestDTO.getContent());
        comment.setRating(requestDTO.getRating());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        comment.setBook(book);

        return comment;
    }

    // 更新評論的資料轉換
    private void editComment(Comment existingComment, CommentUpdateDTO updateDTO){
        existingComment.setContent(updateDTO.getContent());
        existingComment.setRating(updateDTO.getRating());
    }

    // 用 Book / User 的 id 查詢評論的資料轉換
    private CommentSearchByBookDTO convertToSearchBookDTO(Comment comment){
        CommentSearchByBookDTO searchDTO = new CommentSearchByBookDTO();

        searchDTO.setId(comment.getId());
        searchDTO.setUserName(comment.getUser().getName());
        searchDTO.setContent(comment.getContent());
        searchDTO.setRating(comment.getRating());
        searchDTO.setCreatedAt(comment.getCreatedAt());

        return searchDTO;
    }

    private CommentSearchByUserDTO convertToSearchUserDTO(Comment comment){
        CommentSearchByUserDTO searchDTO = new CommentSearchByUserDTO();

        searchDTO.setId(comment.getId());
        searchDTO.setBookTitle(comment.getBook().getTitle());
        searchDTO.setBookAuthor(comment.getBook().getAuthor());
        searchDTO.setContent(comment.getContent());
        searchDTO.setRating(comment.getRating());
        searchDTO.setCreatedAt(comment.getCreatedAt());

        return searchDTO;
    }

    // 將評論事件發送給 Kafka 的方法
//    private void sendCommentEvent(User user, Book book, CommentRequestDTO requestDTO) {
//        CommentKafkaDTO event = new CommentKafkaDTO(
//                user.getId(),
//                book.getId(),
//                requestDTO.getContent(),
//                requestDTO.getRating(),
//                LocalDateTime.now()
//        );
//
//        try {
//            kafkaTemplate.send("comments", event).get(); // 同步等待
//            System.out.println("Message sent to Kafka successfully");
//        } catch (Exception e) {
//            System.err.println("Failed to send message to Kafka: " + e.getMessage());
//            throw new RuntimeException("Kafka send failed", e);
//        }
//    }
//
//    // 處理新增評論事件的方法
//    public void saveComment(CommentKafkaDTO event) {
//        User user = userRepository.findById(event.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + event.getUserId()));
//        Book book = bookRepository.findById(event.getBookId())
//                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + event.getBookId()));
//
//        Comment comment = new Comment();
//        comment.setUser(user);
//        comment.setBook(book);
//        comment.setContent(event.getContent());
//        comment.setRating(event.getRating());
//        comment.setCreatedAt(event.getCreatedAt());
//
//        commentRepository.save(comment);
//    }
}