package com.management.kbbs.service;

import com.management.kbbs.dto.CommentDTO;
import com.management.kbbs.dto.CommentRequestDTO;
import com.management.kbbs.dto.CommentSearchDTO;
import com.management.kbbs.dto.CommentUpdateDTO;
import com.management.kbbs.entity.Book;
import com.management.kbbs.entity.Comment;
import com.management.kbbs.entity.User;
import com.management.kbbs.repository.BookRepository;
import com.management.kbbs.repository.CommentRepository;

import com.management.kbbs.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

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
    public CommentDTO createComment(CommentRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                                  .orElseThrow(() -> new RuntimeException("User not found with ID: " + requestDTO.getUserId()));
        Book book = bookRepository.findById(requestDTO.getBookId())
                                  .orElseThrow(() -> new RuntimeException("Book not found with ID: " + requestDTO.getBookId()));

        Comment savedComment = commentRepository.save(setNewComment(user, book, requestDTO));
        return convertToDTO(savedComment);
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
    public List<CommentSearchDTO> getCommentsByBookId(Long bookId) {
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream()
                       .map(this::convertToSearchDTO)
                       .collect(Collectors.toList());
    }


    // 查詢特定用戶留下的評論
    public List<CommentSearchDTO> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                       .map(this::convertToSearchDTO)
                       .collect(Collectors.toList());
    }





    // 將 Comment 轉換成 CommentDTO
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        commentDTO.setUser(comment.getUser());
        commentDTO.setBook(comment.getBook());
        commentDTO.setContent(comment.getContent());
        commentDTO.setRating(comment.getRating());
        commentDTO.setCreatedAt(comment.getCreatedAt());

        return commentDTO;
    }

    // 新增評論的資料轉換
    private Comment setNewComment(User user, Book book, CommentRequestDTO requestDTO){
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setBook(book);
        comment.setContent(requestDTO.getContent());
        comment.setRating(requestDTO.getRating());
        comment.setCreatedAt(LocalDateTime.now());

        return comment;
    }

    // 更新評論的資料轉換
    private void editComment(Comment existingComment, CommentUpdateDTO updateDTO){
        existingComment.setContent(updateDTO.getContent());
        existingComment.setRating(updateDTO.getRating());
    }

    // 用 Book / User 的 id 查詢評論的資料轉換
    private CommentSearchDTO convertToSearchDTO(Comment comment){
        CommentSearchDTO searchDTO = new CommentSearchDTO();

        searchDTO.setId(comment.getId());
        searchDTO.setUserName(comment.getUser().getName());
        searchDTO.setBookTitle(comment.getBook().getTitle());
        searchDTO.setBookAuthor(comment.getBook().getAuthor());
        searchDTO.setContent(comment.getContent());
        searchDTO.setRating(comment.getRating());
        searchDTO.setCreatedAt(comment.getCreatedAt());

        return searchDTO;
    }
}