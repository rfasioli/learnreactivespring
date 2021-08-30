package com.rfasioli.learnreactivespring.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
class SampleHandlerFunctionTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  void flux_approach1() {
    final var integerFlux = webTestClient.get().uri("/functional/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Integer.class)
        .getResponseBody();

    StepVerifier.create(integerFlux.log())
        .expectSubscription()
        .expectNext(1, 2, 3, 4)
        .verifyComplete();
  }

  @Test
  void mono() {
    Integer expectedValue = 1;

    webTestClient.get().uri("/functional/mono")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Integer.class)
        .consumeWith((response) -> {
          assertEquals(expectedValue, response.getResponseBody());
        });
  }
}