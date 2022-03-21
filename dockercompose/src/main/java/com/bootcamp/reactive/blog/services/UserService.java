package com.bootcamp.reactive.blog.services;

import com.bootcamp.reactive.blog.entities.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> findById(String id);
    Mono<Boolean> existsByLogin(String login);
    Mono<User> findByLogin(String login);
    Flux<User> findAll();
    Mono<User> save(User user);
    Mono<User> saveWithValidation(User user);
    Mono<Void> delete(String id);
    Mono<Boolean> existsByAuthorId(String authorId);
    Mono<User> login(User user);
}
