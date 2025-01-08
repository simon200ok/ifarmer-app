package com.ifarmr.service;

import com.ifarmr.payload.request.CommentRequest;
import com.ifarmr.payload.response.CommentResponse;

public interface CommentService {
    CommentResponse addComment(Long postId, CommentRequest request);
}
