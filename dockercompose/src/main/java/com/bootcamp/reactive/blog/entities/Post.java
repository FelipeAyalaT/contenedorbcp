package com.bootcamp.reactive.blog.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(value="posts")
public class Post {
    private String id;
    private String title;
    private LocalDateTime date;
    private String status;
    private String content;
    private String blogId;
    private List<Comment> comments;
    private List<Reactions> reactions;
}
