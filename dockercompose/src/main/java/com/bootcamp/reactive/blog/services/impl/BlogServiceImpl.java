package com.bootcamp.reactive.blog.services.impl;


import com.bootcamp.reactive.blog.core.exception.BlogExistsException;
import com.bootcamp.reactive.blog.entities.Blog;
import com.bootcamp.reactive.blog.repositories.AuthorRepository;
import com.bootcamp.reactive.blog.repositories.BlogRepository;
import com.bootcamp.reactive.blog.services.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private AuthorRepository authorRepository;
    
    @Override
    public Mono<Blog> findById(String id) {
        return  this.blogRepository.findById(id);
    }

    @Override
    public Flux<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Mono<Blog> save(Blog blog) {
    	if (!"activo".equals(blog.getStatus()) &&
    			!"inactivo".equals(blog.getStatus())) {
    		return Mono.error(new BlogExistsException("Blog status incorrecto"));
    	}
    	
    	return authorRepository.findById(blog.getAuthorId())
    			.flatMap(a -> 
                !a.isMayorEdad()
                    ? Mono.error(new BlogExistsException("Solo pueden tener blogs los autores mayores de 18 años.")) 
                    : blogRepository.findByAuthorId(blog.getAuthorId())
                		.filter(st -> st.getStatus().equals("activo"))
                		.collectList()
                        .flatMap(s -> 
                            s.size()>=3
                                ? Mono.error(new BlogExistsException("Un autor puede tener máximo 03 blogs.")) 
                                : blogRepository.save(blog))
    					); 
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.blogRepository.findById(id)
                .doOnNext(b->{
                    System.out.println("doOnNext b = " + b);
                })
                .flatMap(blog-> this.blogRepository.delete(blog));

    }
}
