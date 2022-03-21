package com.bootcamp.reactive.blog.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(value="user")
public class User {
    @Id
    private String id;
    private String login;
    private String password;
    private String authorId;
    private Boolean conectado;
}