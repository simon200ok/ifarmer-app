package com.ifarmr.service;

import com.ifarmr.payload.request.PostRequest;
import com.ifarmr.payload.response.PostResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    PostResponse createPost(PostRequest request, MultipartFile file);
}
