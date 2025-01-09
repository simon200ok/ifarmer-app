package com.ifarmr.service;

import com.ifarmr.entity.Post;
import com.ifarmr.payload.request.PostRequest;
import com.ifarmr.payload.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest request, MultipartFile file);

    List<Post> getPopularPosts();
}
