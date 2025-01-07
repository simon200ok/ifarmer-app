package com.ifarmr.service.impl;

import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.Post;
import com.ifarmr.entity.User;
import com.ifarmr.payload.request.PostRequest;
import com.ifarmr.payload.response.PostInfo;
import com.ifarmr.payload.response.PostResponse;
import com.ifarmr.repository.PostRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.CloudinaryService;
import com.ifarmr.service.PostService;
import com.ifarmr.utils.AccountUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final JwtService jwtService;
    private final HttpServletRequest servletRequest;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public PostResponse createPost(PostRequest request, MultipartFile file) {


        String token = jwtAuthenticationFilter.getTokenFromRequest(servletRequest);

        // Check if the token is null or empty
        if (token == null || token.isEmpty()) {
            return PostResponse.builder()
                    .responseCode(AccountUtils.EMPTY_TOKEN_CODE)
                    .responseMessage(AccountUtils.EMPTY_TOKEN_MESSAGE)
                    .build();
        }

        // Validate the token
        if (!jwtService.validateToken(token)) {
            return PostResponse.builder()
                    .responseCode(AccountUtils.INVALID_TOKEN_CODE)
                    .responseMessage(AccountUtils.INVALID_TOKEN_MESSAGE)
                    .build();
        }

        if (jwtService.isBlacklisted(token)) {
            return PostResponse.builder()
                    .responseCode(AccountUtils.BLACKLISTED_TOKEN_CODE)
                    .responseMessage(AccountUtils.BLACKLISTED_TOKEN_MESSAGE)
                    .build();
        }


        Long userId = jwtService.extractUserIdFromToken(token);
        if (userId == null) {
            return PostResponse.builder()
                    .responseCode("401")
                    .responseMessage("Unauthorized: Unable to extract userId from token")
                    .build();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));



        String uploadedImageUrl = null;

        if (file != null && !file.isEmpty()) {
            uploadedImageUrl = cloudinaryService.uploadFile(file);
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .image(uploadedImageUrl)
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);

        return PostResponse.builder()
                .responseCode(AccountUtils.POST_SUCCESS_CODE)
                .responseMessage(AccountUtils.POST_SUCCESS_MESSAGE)
                .postInfo(PostInfo.builder()
                        .title(savedPost.getTitle())
                        .description(savedPost.getDescription())
                        .image(savedPost.getImage())
                        .build())
                .build();

    }
}
