package com.bootcamp.reactive.blog.routes;

import com.bootcamp.reactive.blog.core.exception.GlobalExceptionHandler;
import com.bootcamp.reactive.blog.entities.Author;
import com.bootcamp.reactive.blog.entities.Blog;
import com.bootcamp.reactive.blog.handlers.AuthorHandler;
import com.bootcamp.reactive.blog.handlers.BlogHandler;
import com.bootcamp.reactive.blog.handlers.PostHandler;
import com.bootcamp.reactive.blog.handlers.UserHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
public class RouterConfiguration {

    @Bean
    @RouterOperations(
        {
            @RouterOperation(
                path = "/blogs",
                produces = {MediaType.APPLICATION_JSON_VALUE},
                method = RequestMethod.GET,
                beanClass = BlogHandler.class,
                beanMethod = "findAll",
                operation = @Operation(
                        summary = "Listar Blogs",
                        description="Listar Blogs en general",
                        operationId = "findAll",
                        responses = {
                                @ApiResponse(responseCode = "200",
                                        description = "successful operation",
                                        content = @Content(array=@ArraySchema(schema = @Schema(implementation = Blog.class)))),
                                @ApiResponse(
                                        responseCode = "404",
                                        description = "No se encontró elementos",
                                        content = @Content(schema = @Schema(implementation= GlobalExceptionHandler.HttpError.class))
                                )
                        },
                        parameters = {
                                //@Parameter(in = ParameterIn.QUERY, name = "search", required = false )
                                })

            ),
            @RouterOperation(path = "/blogs/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = BlogHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "findById",
                            responses = {
                                    @ApiResponse(responseCode = "200",
                                            description = "successful operation",
                                            content = @Content(schema = @Schema(implementation = Blog.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid Blogs ID supplied"),
                                    @ApiResponse(responseCode = "404", description = "Blog not found")},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id" )})
            ),
            @RouterOperation(path = "/blogs",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = BlogHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "save",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation",
                                            content = @Content(schema = @Schema(implementation = Blog.class)))},
                            parameters = {},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Blog.class))))
            )

        })
     
    public RouterFunction<ServerResponse> blogRoutes(BlogHandler blogHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/blogs"),
                RouterFunctions
                        .route(GET(""), blogHandler::findAll)
                        .andRoute(GET("/{id}"), blogHandler::findById)
                        .andRoute(POST("").and(contentType(APPLICATION_JSON)), blogHandler::save)
//						.andRoute(PUT("/{id}").and(contentType(APPLICATION_JSON)), blogHandler::update)
                        .andRoute(DELETE("/{id}"), blogHandler::delete)
            );
    }

    
    ////AUTOR
    
    @Bean
    @RouterOperations(
        {
            @RouterOperation(path = "/authors/{id}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = AuthorHandler.class,
                    beanMethod = "findById",
                    operation = @Operation(
                            operationId = "findById",
                            responses = {
                                    @ApiResponse(responseCode = "200",
                                            description = "successful operation",
                                            content = @Content(schema = @Schema(implementation = Blog.class))),
                                    @ApiResponse(responseCode = "400", description = "Invalid Author ID supplied"),
                                    @ApiResponse(responseCode = "404", description = "Author not found")},
                            parameters = {
                                    @Parameter(in = ParameterIn.PATH, name = "id" )})
            ),
            @RouterOperation(path = "/authors",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = AuthorHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "save",
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "successful operation",
                                            content = @Content(schema = @Schema(implementation = Author.class)))},
                            parameters = {},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = Author.class))))
            )

        })
    public RouterFunction<ServerResponse> authorRoutes(AuthorHandler authorHandler){
        return RouterFunctions.nest(RequestPredicates.path("/authors"),
                RouterFunctions
                .route(GET(""), authorHandler::findAll)
                .andRoute(GET("/by-email/{email}"), authorHandler::findByEmail)
                .andRoute(GET("/query"), authorHandler::findByEmail)
//                .andRoute(GET("/query/{email}"), authorHandler::findByEmail)
                .andRoute(GET("/{id}"), authorHandler::findById)
                .andRoute(POST("").and(accept(APPLICATION_JSON)),authorHandler::save)
                .andRoute(DELETE("/{id}"), authorHandler::delete)
            );
    }

    @Bean
    public RouterFunction<ServerResponse> postRoutes(PostHandler postHandler){
        return RouterFunctions.nest(RequestPredicates.path("/posts"),
                RouterFunctions.route(GET(""), postHandler::findAll)
                .andRoute(POST("").and(accept(APPLICATION_JSON)),postHandler::save)
                .andRoute(POST("/comments").and(accept(APPLICATION_JSON)),postHandler::saveComments)
                );
    }

    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler){
        return RouterFunctions.nest(RequestPredicates.path("/users"),
                RouterFunctions
                .route(GET(""), userHandler::findAll)
                .andRoute(GET("/by-email/{login}"), userHandler::findByLogin)
                .andRoute(GET("/query"), userHandler::findByLogin)
                .andRoute(GET("/{id}"), userHandler::findById)
                .andRoute(POST("").and(accept(APPLICATION_JSON)),userHandler::save)
                .andRoute(POST("/login").and(accept(APPLICATION_JSON)),userHandler::login)
                .andRoute(DELETE("/{id}"), userHandler::delete)
            );
    }
}
