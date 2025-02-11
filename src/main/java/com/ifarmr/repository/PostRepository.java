package com.ifarmr.repository;

import com.ifarmr.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    @Query(value = "SELECT * FROM post_tbl ORDER BY likes DESC", nativeQuery = true)
    List<Post> findAllPostsOrderedByLikes();

//    @Query("SELECT p FROM Post p ORDER BY (p.likes + p.comments) DESC")
//    List<Post> findPopularPosts();
}