package com.ifarmr.service.impl;

import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.Post;
import com.ifarmr.entity.User;
import com.ifarmr.payload.response.CommentResponse;
import com.ifarmr.payload.response.LikeResponse;
import com.ifarmr.repository.CommentRepository;
import com.ifarmr.repository.PostRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.LikeService;
import com.ifarmr.utils.AccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HttpServletRequest servletRequest;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Override
    public LikeResponse likePost(Long postId) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(servletRequest);

        // Check if the token is null or empty
        if (token == null || token.isEmpty()) {
            return LikeResponse.builder()
                    .responseCode(AccountUtils.EMPTY_TOKEN_CODE)
                    .responseMessage(AccountUtils.EMPTY_TOKEN_MESSAGE)
                    .build();
        }

        // Validate the token
        if (!jwtService.validateToken(token)) {
            return LikeResponse.builder()
                    .responseCode(AccountUtils.INVALID_TOKEN_CODE)
                    .responseMessage(AccountUtils.INVALID_TOKEN_MESSAGE)
                    .build();
        }

        // Check if the token is blacklisted
        if (jwtService.isBlacklisted(token)) {
            return LikeResponse.builder()
                    .responseCode(AccountUtils.BLACKLISTED_TOKEN_CODE)
                    .responseMessage(AccountUtils.BLACKLISTED_TOKEN_MESSAGE)
                    .build();
        }

        // Extract the userId from the token
        Long userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            return LikeResponse.builder()
                    .responseCode("401")
                    .responseMessage("Unauthorized: Unable to extract userId from token")
                    .build();
        }

        // Find the user by ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

//      Find the post by ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Check if the user has already liked the post
        if (post.getLikedUser().contains(user)) {
            return LikeResponse.builder()
                    .responseCode("409")
                    .responseMessage("User has already liked this post")
                    .build();
        }

        post.getLikedUser().add(user);
        post.setLikes(post.getLikes()+1);

        // Save the updated post
        postRepository.save(post);

        return LikeResponse.builder()
                .responseCode("011")
                .responseMessage("Like Updated successfully")
                .build();
    }


}
