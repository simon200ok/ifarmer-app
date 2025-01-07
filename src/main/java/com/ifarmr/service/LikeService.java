package com.ifarmr.service;

import com.ifarmr.payload.response.LikeResponse;

public interface LikeService {
    LikeResponse likePost(Long postId);
}
