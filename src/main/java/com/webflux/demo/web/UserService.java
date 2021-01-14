package com.webflux.demo.web;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<UserDTO> save(UserDTO data);

    Flux<UserDTO> findByName(String name);

    Mono<Void> deleteById(String id);
}
