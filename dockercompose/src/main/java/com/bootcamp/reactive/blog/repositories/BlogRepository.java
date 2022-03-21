package com.bootcamp.reactive.blog.repositories;

import com.bootcamp.reactive.blog.entities.Blog;

import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BlogRepository extends ReactiveMongoRepository<Blog, String> {
    Flux<Blog> findByAuthorId(String authorId);
}