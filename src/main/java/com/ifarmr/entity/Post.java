package com.ifarmr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "post_tbl")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseClass{
    private String title;

    private String description;

    private List<String> image;

    private int likes = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
