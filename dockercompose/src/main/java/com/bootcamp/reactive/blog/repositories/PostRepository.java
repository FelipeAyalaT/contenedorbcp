package com.bootcamp.reactive.blog.repositories;

import com.bootcamp.reactive.blog.entities.Post;

import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PostRepository extends ReactiveMongoRepository<Post, String> {
	Flux<Post> findByBlogId(String blogId);
}
