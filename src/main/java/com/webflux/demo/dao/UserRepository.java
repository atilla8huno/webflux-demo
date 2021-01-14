package com.webflux.demo.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {
                                //extends ReactiveMongoRepository<User, String> {

    Flux<User> findAllByName(String name);
}
