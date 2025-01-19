package com.ifarmr.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ifarmr.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {

    private Long id;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdAt;
    private String userName;
    private String profilePicture;

    public CommentDto(long id, String content, String string) {
    }

    public CommentDto(long id, String content, String string, Object o) {
    }
}
