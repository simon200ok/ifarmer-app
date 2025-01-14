package com.ifarmr.controller;

import com.ifarmr.entity.User;
import com.ifarmr.payload.request.*;
import com.ifarmr.payload.response.CommentResponse;
import com.ifarmr.payload.response.LikeResponse;
import com.ifarmr.payload.response.PostResponse;
import com.ifarmr.service.CommentService;
import com.ifarmr.service.LikeService;
import com.ifarmr.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getUserPosts(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.getUserPosts(user.getId()));
    }

    @GetMapping("/allPosts")
    public ResponseEntity<List<AllPosts>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailsDto> getPostDetails(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostDetails(postId));
    }


}
