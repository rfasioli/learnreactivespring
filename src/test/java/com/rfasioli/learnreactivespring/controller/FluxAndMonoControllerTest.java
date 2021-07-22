package com.rfasioli.learnreactivespring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest
class FluxAndMonoControllerTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  void getFlux() {
    final var integerFlux = webTestClient.get().uri("/flux")
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
  void getFlux_approach2() {
    final var integerFlux = webTestClient.get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(Integer.class)
        .hasSize(4);
  }

  @Test
  void getFlux_approach3() {
    List<Integer> expected = Arrays.asList(1, 2, 3, 4);

    final var result = webTestClient
        .get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Integer.class)
        .returnResult();

    assertThat(expected)
        .isEqualTo(result.getResponseBody());
  }

  @Test
  void getFlux_approach4() {
    List<Integer> expected = Arrays.asList(1, 2, 3, 4);

    webTestClient
        .get().uri("/flux")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(Integer.class)
        .consumeWith((response) -> {
          assertThat(expected)
              .isEqualTo(response.getResponseBody());
        });

  }

  @Test
  void getFluxStream() {
    final var integerFlux = webTestClient.get().uri("/fluxstream")
        .accept(MediaType.APPLICATION_NDJSON)
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
  void getFluxStream_approach2() {
    final var integerFlux = webTestClient.get().uri("/fluxstream")
        .accept(MediaType.APPLICATION_NDJSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_NDJSON)
        .expectBodyList(Integer.class)
        .hasSize(4);
  }

  @Test
  void getFluxTextStream() {
    final var integerFlux = webTestClient.get().uri("/fluxtextstream")
        .accept(MediaType.TEXT_EVENT_STREAM)
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
  void getFluxInfinityStream() {
    final var longStreamFlux = webTestClient.get().uri("/fluxstreaminfinity")
        .accept(MediaType.APPLICATION_NDJSON)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Long.class)
        .getResponseBody();

    StepVerifier.create(longStreamFlux)
        .expectNext(0L, 1L, 2L)
        .thenCancel()
        .verify();
  }
}