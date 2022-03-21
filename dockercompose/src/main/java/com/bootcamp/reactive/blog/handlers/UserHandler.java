package com.bootcamp.reactive.blog.handlers;

import com.bootcamp.reactive.blog.entities.User;
import com.bootcamp.reactive.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class UserHandler {

    @Autowired
    private UserService userService;

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(userService.findAll(), User.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        var id = request.pathVariable("id");
//        return ServerResponse.ok()
//                .contentType(APPLICATION_JSON)
//                .body(userService.findById(id), User.class);

        return this.userService.findById(id)
                .flatMap(a-> ServerResponse.ok().body(Mono.just(a), User.class))
                .switchIfEmpty(ServerResponse.notFound().build());

    }


    public Mono<ServerResponse> findByLogin(ServerRequest request){
        var login=request.queryParam("login").get();

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(userService.findByLogin(login), User.class);

    }


    public Mono<ServerResponse> save(ServerRequest request){

        var userInput= request.bodyToMono(User.class);

//        return userInput
//                .flatMap(a-> {
//                    return userService.findByLogin(a.getLogin())
//                            .hasElements()
//                            .flatMap(exists-> !exists ?  userService.save(a): Mono.empty());
//                })
//                .switchIfEmpty(Mono.error(new UserExistsException("User exists")))
//                .flatMap(a-> ServerResponse
//                        .ok()
//                        .contentType(APPLICATION_JSON)
//                        .body(Mono.just(a), User.class));




//        return userInput
//                .filterWhen(a-> {
//                    return userService.findByLogin(a.getLogin())
//                            .hasElements()
//                            .map(success-> !success);
//                })
//                .flatMap(a-> {
//                    return userService.save(a);
//                })
//                .switchIfEmpty(Mono.error(new UserExistsException("User exists")))
//                .flatMap(a-> {
//                    return ServerResponse
//                            .ok()
//                            .contentType(APPLICATION_JSON)
//                            .body(Mono.just(a), User.class);
//                });


//
//        return userInput
//                .flatMap(a-> {
//                    return userService.existsByLogin(a.getLogin())
//                            .flatMap(exists-> exists ? Mono.empty():  userService.save(a));
//                })
//                .switchIfEmpty(Mono.error(new UserExistsException("User exists")))
//                .flatMap(a-> ServerResponse
//                        .ok()
//                        .contentType(APPLICATION_JSON)
//                        .body(Mono.just(a), User.class));


        return userInput
                .flatMap(user-> this.userService.saveWithValidation(user))
                .flatMap(a-> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(a), User.class));
//                .switchIfEmpty(Mono.error(new UserExistsException("User exists")));

    }

    public Mono<ServerResponse> login(ServerRequest request){

        var userInput= request.bodyToMono(User.class);
        
        return userInput
                .flatMap(user-> this.userService.login(user))
                .flatMap(a-> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .body(Mono.just(a), User.class));
        
        
    }
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        //String userId = serverRequest.pathVariable("id");

        return this.userService.delete(serverRequest.pathVariable("id"))
                .then(ServerResponse.ok().build());

//
//        return ServerResponse.ok()
//                .contentType(APPLICATION_JSON)
//                .body(userService.delete(userId),User.class);

    }
}
