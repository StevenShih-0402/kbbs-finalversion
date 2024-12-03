package com.management.kbbs.service;

import com.management.kbbs.dto.CommentDTO;
import com.management.kbbs.entity.Comment;
import com.management.kbbs.repository.CommentRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 新增一條評論
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment savedComment = commentRepository.save(setNewComment(commentDTO));
        return convertToDTO(savedComment);
    }

    // 查詢特定的評論
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));
        return convertToDTO(comment);
    }

    // 更新評論
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
                                                   .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        editComment(existingComment, commentDTO);

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
    public List<CommentDTO> getCommentsByBookId(Long bookId) {
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
    }


    // 查詢特定用戶留下的評論
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }





    // 將 Comment 轉換成 CommentDTO
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setBookId(comment.getBookId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setRating(comment.getRating());
        commentDTO.setCreatedAt(comment.getCreatedAt());

        return commentDTO;
    }

    // 新增評論的資料轉換
    private Comment setNewComment(CommentDTO commentDTO){
        Comment comment = new Comment();
        comment.setUserId(commentDTO.getUserId());
        comment.setBookId(commentDTO.getBookId());
        comment.setContent(commentDTO.getContent());
        comment.setRating(commentDTO.getRating());
        comment.setCreatedAt(LocalDateTime.now());

        return comment;
    }

    // 更新評論的資料轉換
    private void editComment(Comment existingComment, CommentDTO commentDTO){
        existingComment.setContent(commentDTO.getContent());
        existingComment.setRating(commentDTO.getRating());
    }
}