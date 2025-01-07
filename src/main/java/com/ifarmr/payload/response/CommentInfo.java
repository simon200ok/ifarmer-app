package com.ifarmr.payload.response;

import com.ifarmr.entity.Post;
import com.ifarmr.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentInfo {
    private Long id;
    private Post postId;
    private String content;
    private User userId;
    private LocalDateTime createdAt;
}
