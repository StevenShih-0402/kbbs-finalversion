package com.management.kbbs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleCommentDTO {
    private Long commentId;
    private String booktitle;
    private String bookauthor;
    private String comment;
    private Integer rating;
}
