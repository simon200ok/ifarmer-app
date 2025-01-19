package com.ifarmr.service.impl;

import com.ifarmr.auth.service.JwtAuthenticationFilter;
import com.ifarmr.auth.service.JwtService;
import com.ifarmr.entity.Post;
import com.ifarmr.entity.User;
import com.ifarmr.exception.customExceptions.ResourceNotFoundException;
import com.ifarmr.payload.request.AllPosts;
import com.ifarmr.payload.request.PostDetailsDto;
import com.ifarmr.payload.request.PostDto;
import com.ifarmr.payload.request.PostRequest;
import com.ifarmr.payload.response.CommentDto;
import com.ifarmr.payload.response.PostInfo;
import com.ifarmr.payload.response.PostResponse;
import com.ifarmr.repository.PostRepository;
import com.ifarmr.repository.UserRepository;
import com.ifarmr.service.CloudinaryService;
import com.ifarmr.service.PostService;
import com.ifarmr.utils.AccountUtils;
import com.ifarmr.utils.ExtractUserID;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final JwtService jwtService;
    private final HttpServletRequest servletRequest;
    private final ExtractUserID extractUserID;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public PostResponse createPost(PostRequest request, MultipartFile file) {

        long userId = extractUserID.getUserIdFromToken(servletRequest);
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
                .likes(0L)
                .commentCount(0L)
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

    @Override
    public List<PostDto> getUserPosts(long id) {
        return postRepository.findByUserId(id).stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getImage(),
                        post.getLikes(),
                        post.getCommentCount()))
                .collect(Collectors.toList());
    }

    @Override
    public PostDetailsDto getPostDetails(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));

        List<CommentDto> commentDtos = post.getComments().stream()
                .map(comment -> new CommentDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt().toString(),
                        comment.getUser().getUsername(),
                        comment.getUser().getDisplayPhoto()

                ))
                .collect(Collectors.toList());

        return new PostDetailsDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImage(),
                post.getUser().getUsername(),
                post.getUser().getDisplayPhoto(),
                post.getLikes(),
                post.getCommentCount(),
                commentDtos
        );
    }

    @Override
    public List<AllPosts> getAllPosts() {
        return postRepository.findAll().
                stream()
                .map(post -> new AllPosts(
                        post.getTitle())
                ).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPopularPosts() {
        return postRepository.findAllPostsOrderedByLikes().stream()
                .map(post -> new PostDto(
                        post.getId(),
                        post.getTitle(),
                        post.getDescription(),
                        post.getImage(),
                        post.getLikes(),
                        post.getCommentCount()))
                .collect(Collectors.toList());
    }

}
