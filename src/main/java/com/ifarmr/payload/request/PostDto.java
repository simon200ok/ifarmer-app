package com.ifarmr.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String image;
    private Long likes;
    private Long comments;

    public PostDto(long id, @NotBlank(message = "Title is required") @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters") String title, @NotBlank(message = "Description is required") String description) {
    }
}