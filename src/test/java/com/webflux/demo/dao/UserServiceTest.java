package com.webflux.demo.dao;

import com.webflux.demo.web.UserDTO;
import com.webflux.demo.web.UserService;
import com.webflux.demo.web.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;

public class UserServiceTest {

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(mock(UserRepository.class));
    }

    @Test
    @DisplayName("Should validate parameter")
    void givenInvalidId_whenDeleteById_thenShouldThrowException() {
        //given
        String id = null;

        //when
        Executable deleteById = () -> service.deleteById(id);

        //then
        Assertions.assertThrows(NullPointerException.class, deleteById);
    }

    @Test
    @DisplayName("Should validate name when searching by")
    void givenShortName_whenFindByName_thenShouldThrowException() {
        //given
        String name = "Gez";

        //when
        Flux<UserDTO> findByName = service.findByName(name);

        //then
        StepVerifier
                .create(findByName)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
