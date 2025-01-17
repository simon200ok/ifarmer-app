package com.ifarmr.payload.request;


import com.ifarmr.entity.Comment;
import com.ifarmr.payload.response.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDetailsDto {

    private Long id;
    private String title;
    private String content;
    private Long likes;
    private Long commentCount;
    private List<CommentDto> comments;

}
