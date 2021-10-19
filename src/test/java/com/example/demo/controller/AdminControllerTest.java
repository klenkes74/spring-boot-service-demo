package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rlichti
 * @version 1.0.0 2021-10-18
 * @since 1.0.0 2021-10-18
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser
public class AdminControllerTest {

    @Inject
    private TestRestTemplate rest;

    @Test
    public void shouldReturnPongWhenCallingPing() {
        ResponseEntity<String> response = rest
                .withBasicAuth("user", "password")
                .getForEntity("/api/admin/ping", String.class);

        assertThat(response.getBody()).isEqualTo("pong");
    }
}
