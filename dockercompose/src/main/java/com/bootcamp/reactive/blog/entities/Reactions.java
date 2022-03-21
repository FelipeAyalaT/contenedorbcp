package com.bootcamp.reactive.blog.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@Document(value="reactions")
public class Reactions {
    @Id
    private String id;
    private String type;
    private Date date;
    private String userId; 
//    private String postId; 
}
