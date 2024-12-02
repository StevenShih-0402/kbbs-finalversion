package com.management.kbbs.service;

import com.management.kbbs.dto.CommentDTO;
import com.management.kbbs.entity.Comment;
import com.management.kbbs.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // Create a new comment
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUserId(commentDTO.getUserId());
        comment.setBookId(commentDTO.getBookId());
        comment.setContent(commentDTO.getContent());
        comment.setRating(commentDTO.getRating());
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return CommentDTO.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .bookId(comment.getBookId())
                .content(comment.getContent())
                .rating(comment.getRating())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // Get a comment by ID
    public CommentDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        return CommentDTO.builder()
                .id(comment.getId())
                .userId(comment.getUserId())
                .bookId(comment.getBookId())
                .content(comment.getContent())
                .rating(comment.getRating())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    // Update a comment
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with ID: " + id));

        existingComment.setContent(commentDTO.getContent());
        existingComment.setRating(commentDTO.getRating());

        Comment updatedComment = commentRepository.save(existingComment);

        return CommentDTO.builder()
                .id(updatedComment.getId())
                .userId(updatedComment.getUserId())
                .bookId(updatedComment.getBookId())
                .content(updatedComment.getContent())
                .rating(updatedComment.getRating())
                .createdAt(updatedComment.getCreatedAt())
                .build();
    }

    // Delete a comment
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found with ID: " + id);
        }
        commentRepository.deleteById(id);
    }

    // Get all comments
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll()
                .stream()
                .map(comment -> CommentDTO.builder()
                        .id(comment.getId())
                        .userId(comment.getUserId())
                        .bookId(comment.getBookId())
                        .content(comment.getContent())
                        .rating(comment.getRating())
                        .createdAt(comment.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }

    // Get comments by book ID
    public List<CommentDTO> getCommentsByBookId(Long bookId) {
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream()
                .map(comment -> CommentDTO.builder()
                        .id(comment.getId())
                        .userId(comment.getUserId())
                        .bookId(comment.getBookId())
                        .content(comment.getContent())
                        .rating(comment.getRating())
                        .createdAt(comment.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }


    // Get comments by user ID
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream()
                .map(comment -> CommentDTO.builder()
                        .id(comment.getId())
                        .userId(comment.getUserId())
                        .bookId(comment.getBookId())
                        .content(comment.getContent())
                        .rating(comment.getRating())
                        .createdAt(comment.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }
}