package com.management.kbbs.exception;

import com.management.kbbs.dto.CommentKafkaDTO;
import com.management.kbbs.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "comments", groupId = "comment-group")
@RequiredArgsConstructor
public class CommentEventListener {

    private final CommentService commentService;

    @KafkaHandler
    public void handleCommentEvent(CommentKafkaDTO event) {
        // 模擬後台處理事件
        System.out.println("Processing comment event: " + event);

        // 更新資料庫
        commentService.saveComment(event);
    }
}