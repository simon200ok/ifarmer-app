package com.ifarmr.payload.request;


import com.ifarmr.entity.Comment;
import com.ifarmr.payload.response.CommentDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private int likes;
    private List<CommentDto> comments;

    public PostDetailsDto(long id, @NotBlank(message = "Title is required") @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters") String title, @NotBlank(message = "Description is required") String description, int likes, List<Comment> comments) {
    }
}
