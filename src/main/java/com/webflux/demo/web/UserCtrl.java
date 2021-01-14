package com.webflux.demo.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping(
        path = "/user"
)
@AllArgsConstructor
public class UserCtrl {

    private final UserService service;

    @PostMapping("/create")
    public Mono<UserDTO> create(@RequestBody UserDTO data) {
        return service.save(data)
                .doOnSuccess(user -> log.info("User created: {}", user))
                .doOnError(error -> log.error("Error creating user", error));
    }

    @PutMapping("/update/{id}")
    public Mono<UserDTO> update(@RequestBody UserDTO data,
                                @PathVariable String id) {
        return Mono.just(id)
                .filter(Objects::nonNull)
                .map(idParam -> {
                    data.setId(idParam);
                    return data;
                })
                .flatMap(service::save)
                .doOnSuccess(user -> log.info("User updated: {}", user))
                .doOnError(error -> log.error("Error updating user", error));
    }

    @GetMapping("/all")
    public Flux<UserDTO> get(@RequestParam String name) {
        return service.findByName(name)
                .doOnNext(user -> log.info("User found by name '{}': {}", name, user))
                .doOnError(error -> log.error("Error listing user", error));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return service.deleteById(id)
                .doOnSuccess(user -> log.info("User deleted: ID {}", id))
                .doOnError(error -> log.error("Error deleting user", error));
    }
}
