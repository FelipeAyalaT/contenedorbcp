package com.bootcamp.reactive.blog.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.bootcamp.reactive.blog.core.exception.BlogExistsException;
import com.bootcamp.reactive.blog.core.exception.GeneralExistsException;
import com.bootcamp.reactive.blog.entities.Post;
import com.bootcamp.reactive.blog.repositories.BlogRepository;
import com.bootcamp.reactive.blog.repositories.PostRepository;
import com.bootcamp.reactive.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Mono<Post> save(Post post) {
        
        if (!"borrador".equals(post.getStatus()) &&
    			!"publicado".equals(post.getStatus())) {
    		return Mono.error(new BlogExistsException("Post status incorrecto"));
    	}
        
        if (!DateTimeFormatter.ofPattern("yyyy-MM-dd").
					 format(post.getDate()).equals(DateTimeFormatter.ofPattern("yyyy-MM-dd")
						 .format(LocalDate.now())))  {
    		return Mono.error(new BlogExistsException("Debe registrar post con fecha del día."));
    	}
    	
    	return blogRepository.findById(post.getBlogId())
    			.flatMap(a -> 
                !a.getStatus().equals("activo")
                    ? Mono.error(new GeneralExistsException("Solo se puede registrar posts en blogs en estado activo.")) 
                    : postRepository.findByBlogId(post.getBlogId())
                		.filter(x-> DateTimeFormatter.ofPattern("yyyy-MM-dd").
       						 format(x.getDate()).equals(DateTimeFormatter.ofPattern("yyyy-MM-dd")
    								 .format(LocalDate.now())) &&
    						 x.getStatus().equals("publicado"))
                		.collectList()
                        .flatMap(s -> 
                            s.size()>0
                                ? Mono.error(new GeneralExistsException("Solo se puede publicar un post por día.")) 
                                : postRepository.save(post))
    					); 
    	
    }
    
    @Override
    public Mono<Post> saveComments(Post post) { 
    	return postRepository.findById(post.getId())
    			.flatMap(a ->  
    					 !a.getStatus().equals("publicado") ?
    						Mono.error(new GeneralExistsException("Solo se pueden registrar comentarios en post en estado publicado.")) : 
	                		postRepository.save(a)
                ); 
    	
    }

    @Override
    public Flux<Post> findAll() {
        return this.postRepository.findAll();
    }
}
