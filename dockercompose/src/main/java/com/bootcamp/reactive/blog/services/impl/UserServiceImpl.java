package com.bootcamp.reactive.blog.services.impl;

import com.bootcamp.reactive.blog.core.exception.AuthorNotFoundException;
import com.bootcamp.reactive.blog.core.exception.GeneralExistsException;
import com.bootcamp.reactive.blog.core.exception.GeneralNotFoundException;
import com.bootcamp.reactive.blog.entities.User;
import com.bootcamp.reactive.blog.repositories.UserRepository;
import com.bootcamp.reactive.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Mono<User> findById(String id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Mono<Boolean> existsByLogin(String email) {
        return userRepository.existsByLogin(email);
    }

    @Override
    public Mono<User> findByLogin(String email) {
//        var userFilter = new User();
//        userFilter.setLogin(email);
//
//        return this.userRepository.findAll(Example.of(userFilter));

        return this.userRepository.findByLogin(email);
    }

    @Override
    public Flux<User> findAll() {
        return this.userRepository.findAll();
    }

    @Override
    public Mono<User> save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public Mono<User> saveWithValidation(User user) { 
    			
        return this.userRepository.existsByLogin(user.getLogin())
                .flatMap(exists->
                {
                	 if (!exists)
                	{
                		return this.userRepository.existsByAuthorId(user.getAuthorId()).flatMap(exists1->
                        {
                        	return !exists1 ? this.userRepository.save(user): Mono.error(new GeneralExistsException("Un autor debe tener solo un usuario."));
                        });
                	}  
                	return  Mono.error(new GeneralExistsException("El usuario ya existe."));
                }); 	
    }
    
    @Override
    public Mono<User> login(User user) { 
    	 return this.userRepository.findByLogin(user.getLogin())
                .switchIfEmpty(Mono.error(new GeneralNotFoundException("usuario no encontrado")))
                .flatMap(users-> {
                	users.setConectado(true);
                    return users.getPassword().
                    		equals(user.getPassword()) ? this.userRepository.save(users) : Mono.error(new GeneralExistsException("Credenciales incorrectas"));
                });	 
    }

    @Override
    public Mono<Void> delete(String id) {

        return this.userRepository.findById(id)
            .switchIfEmpty(Mono.error(new GeneralNotFoundException("User no encontrado")))
            .flatMap(user-> {
                return this.userRepository.delete(user);
            });  
    }

	@Override
	public Mono<Boolean> existsByAuthorId(String authorId) {
		// TODO Auto-generated method stub
		return null;
	}

}
