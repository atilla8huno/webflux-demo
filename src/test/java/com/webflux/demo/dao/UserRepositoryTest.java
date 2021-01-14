package com.webflux.demo.dao;

import com.webflux.demo.DemoApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@DataMongoTest
@ContextConfiguration(classes = {DemoApplication.class})
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @AfterEach
    void cleanup() {
        repository.deleteAll().block();
    }

    @Test
    @DisplayName("Should save a document on the database")
    void givenDocument_whenSave_thenShouldBeSavedOnDatabase() {
        //given
        User user = User.builder()
                .id("123")
                .name("Hello there")
                .build();

        //when
        Mono<User> save = repository.save(user);

        //then
        StepVerifier.create(save)
                .assertNext(savedUser -> assertTrue("Both users are equal",
                        user.equals(savedUser)))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Should list all users by name")
    void givenName_whenFindAllByName_thenShouldGetListOfUsers() {
        //given
        String jesus = "jesus";

        User joseph = User.builder().id("1").name("Joseph").build();
        User mary = User.builder().id("2").name("Mary").build();
        User jesus1 = User.builder().id("3").name(jesus).build();
        User jesus2 = User.builder().id("4").name(jesus).build();

        repository.save(joseph).block();
        repository.save(mary).block();
        repository.save(jesus1).block();
        repository.save(jesus2).block();

        //when
        Flux<User> users = repository.findAllByName(jesus);

        //then
        StepVerifier
                .create(users)
                .assertNext(user -> assertTrue("Jesus found",
                        jesus.equals(user.getName())))
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }
}
