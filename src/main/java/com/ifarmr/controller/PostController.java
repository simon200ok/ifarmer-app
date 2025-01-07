package com.ifarmr.controller;

import com.ifarmr.payload.request.CommentRequest;
import com.ifarmr.payload.request.PostRequest;
import com.ifarmr.payload.response.CommentResponse;
import com.ifarmr.payload.response.LikeResponse;
import com.ifarmr.payload.response.PostResponse;
import com.ifarmr.service.CommentService;
import com.ifarmr.service.LikeService;
import com.ifarmr.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;

    @PostMapping(value = "/create-post", consumes = "multipart/form-data")
    public ResponseEntity<PostResponse> createPost(
            @RequestPart("post") @Valid PostRequest request,
            @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(postService.createPost(request, file));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId, @RequestBody CommentRequest request) {
        return ResponseEntity.ok( commentService.addComment(postId, request));
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<LikeResponse> likePost(@PathVariable Long postId) {
        LikeResponse updatedLikeCount = likeService.likePost(postId);
        return ResponseEntity.ok(updatedLikeCount);
    }


}
