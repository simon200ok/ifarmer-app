package com.ifarmr.service.impl;

import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.Comment;
import com.ifarmr.entity.Post;
import com.ifarmr.entity.User;
import com.ifarmr.payload.request.CommentRequest;
import com.ifarmr.payload.request.NotificationRequest;
import com.ifarmr.payload.response.CommentInfo;
import com.ifarmr.payload.response.CommentResponse;
import com.ifarmr.payload.response.LikeResponse;
import com.ifarmr.payload.response.PostResponse;
import com.ifarmr.repository.CommentRepository;
import com.ifarmr.repository.PostRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.CommentService;
import com.ifarmr.service.NotificationService;
import com.ifarmr.utils.AccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtService jwtService;
    private final HttpServletRequest servletRequest;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final NotificationService notificationService;

    @Override
    public CommentResponse addComment(Long postId, CommentRequest request) {
        String token = jwtAuthenticationFilter.getTokenFromRequest(servletRequest);

        if (token == null || token.isEmpty()) {
            return CommentResponse.builder()
                    .responseCode(AccountUtils.EMPTY_TOKEN_CODE)
                    .responseMessage(AccountUtils.EMPTY_TOKEN_MESSAGE)
                    .build();
        }

        if (!jwtService.validateToken(token)) {
            return CommentResponse.builder()
                    .responseCode(AccountUtils.INVALID_TOKEN_CODE)
                    .responseMessage(AccountUtils.INVALID_TOKEN_MESSAGE)
                    .build();
        }

        if (jwtService.isBlacklisted(token)) {
            return CommentResponse.builder()
                    .responseCode(AccountUtils.BLACKLISTED_TOKEN_CODE)
                    .responseMessage(AccountUtils.BLACKLISTED_TOKEN_MESSAGE)
                    .build();
        }


        Long userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            return CommentResponse.builder()
                    .responseCode("401")
                    .responseMessage("Unauthorized: Unable to extract userId from token")
                    .build();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .user(user)
                .post(post)
                .build();

        post.setCommentCount(post.getCommentCount()+1);

        postRepository.save(post);
        Comment savedComment = commentRepository.save(comment);

        User postOwner = post.getUser(); // Assuming the post has a user field pointing to the owner
        Map<String, String> commentDetails = Map.of(
                "commenterId", String.valueOf(user.getId()),
                "commentText", request.getContent()
        );
        NotificationRequest commentNotificationRequest = NotificationRequest.builder()
                .userId(postOwner.getId())
                .eventType("NEW_COMMENT")
                .eventDetails(commentDetails)
                .build();

        notificationService.sendNotification(commentNotificationRequest); // Send the comment notification



        return CommentResponse.builder()
                .responseCode(AccountUtils.COMMENT_SUCCESS_CODE)
                .responseMessage(AccountUtils.COMMENT_SUCCESS_MESSAGE)
                .commentInfo(CommentInfo.builder()
                        .id(savedComment.getId())
                        .content(savedComment.getContent())
                        .createdAt(LocalDateTime.now())
                        .build())
                .build();

    }
}
