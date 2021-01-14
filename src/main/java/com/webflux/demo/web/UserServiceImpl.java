package com.webflux.demo.web;

import com.webflux.demo.dao.User;
import com.webflux.demo.dao.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository dao;

    @Override
    public Mono<UserDTO> save(UserDTO data) {
        return Mono.just(data)
                .map(dto -> User.builder()
                        .id(dto.getId())
                        .name(dto.getName())
                        .build())
                .flatMap(dao::save)
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build());
    }

    @Override
    public Flux<UserDTO> findByName(String name) {
        return Flux.just(name)
                .doOnNext(nameParam -> {
                    if (nameParam.length() < 4) {
                        throw new IllegalArgumentException("Name param is illegal");
                    }
                })
                .flatMap(dao::findAllByName)
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .build());
    }

    @Override
    public Mono<Void> deleteById(@NonNull String id) {
        return dao.deleteById(id);
    }
}
