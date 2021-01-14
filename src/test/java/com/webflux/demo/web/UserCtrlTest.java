package com.webflux.demo.web;

import com.webflux.demo.dao.User;
import com.webflux.demo.dao.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebFluxTest
public class UserCtrlTest {

    @Autowired
    private WebTestClient httpClient;

    @MockBean
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        mockRepository();
    }

    @Test
    @DisplayName("Should save using API")
    void givenData_whenCreateEndPoint_thenShouldBeSaved() {
        //given
        UserDTO request = UserDTO.builder().id("1").name("Jesus").build();

        //when
        httpClient.post()
                .uri("/user/create")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                //then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserDTO.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getStatus().is2xxSuccessful());
                    Assertions.assertEquals(request, response.getResponseBody());
                });
    }

    @Test
    @DisplayName("Should list using API")
    void givenName_whenGetAllEndPoint_thenShouldGetAllUsers() {
        //given
        String name = "Jesus";

        //when
        httpClient.get()
                .uri("/user/all?name=" + name)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                //then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(UserDTO.class)
                .consumeWith(response -> {
                    Assertions.assertTrue(response.getStatus().is2xxSuccessful());

                    List<UserDTO> users = response.getResponseBody();

                    Assertions.assertNotNull(users);
                    Assertions.assertFalse(users.isEmpty());
                    Assertions.assertEquals(name, users.get(0).getName());
                });
    }

    private void mockRepository() {
        Mockito.when(repository.findAllByName(eq("Jesus")))
                .thenReturn(Flux.just(User.builder().id("1").name("Jesus").build()));

        Mockito.when(repository.save(any()))
                .thenReturn(Mono.just(User.builder().id("1").name("Jesus").build()));
    }
}
