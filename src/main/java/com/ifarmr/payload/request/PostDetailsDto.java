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
    private String postImage;
    private String postOwner;
    private String postOwnerPicture;
    private Long likes;
    private Long commentCount;
    private List<CommentDto> comments;

    public PostDetailsDto(long id, @NotBlank(message = "Title is required") @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title, @NotBlank(message = "Description is required") String description, Long likes, Long commentCount, List<CommentDto> commentDtos) {
    }

    public PostDetailsDto(long id, @NotBlank(message = "Title is required") @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title, @NotBlank(message = "Description is required") String description, String image, Long likes, Long commentCount, List<CommentDto> commentDtos) {
    }

    public PostDetailsDto(long id, @NotBlank(message = "Title is required") @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title, @NotBlank(message = "Description is required") @Size(max = 500, message = "Description must not exceed 255 characters") String description, Long likes, List<Comment> comments) {
    }
}
