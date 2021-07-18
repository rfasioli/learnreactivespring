package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

class VirtualTimeTest {

  @Test
  void testingWithoutVirtualTime() {
    final var longFlux = Flux.interval(Duration.ofSeconds(1))
        .take(3);

    StepVerifier.create(longFlux.log())
        .expectSubscription()
        .expectNext(0L, 1L, 2L)
        .verifyComplete();
  }

  @Test
  void testingWithVirtualTime() {
    VirtualTimeScheduler.getOrSet();

    final var longFlux = Flux.interval(Duration.ofSeconds(1))
        .take(3);

    StepVerifier.withVirtualTime(longFlux::log)
        .expectSubscription()
        .thenAwait(Duration.ofSeconds(3))
        .expectNext(0l, 1l, 2l)
        .verifyComplete();
  }
}
