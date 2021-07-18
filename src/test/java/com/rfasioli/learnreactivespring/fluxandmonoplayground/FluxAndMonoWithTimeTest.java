package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FluxAndMonoWithTimeTest {
  @Test
  void infiniteSequence() throws InterruptedException {
    final var infiniteFlux = Flux.interval(Duration.ofMillis(100))
        .log();

    // NonBlocking, but consume flux until current thread still running...
    infiniteFlux.subscribe((e) -> System.out.println("Value is: " + e));

    Thread.sleep(3000); // Keep application still running for 3 secs

    assertTrue(true);
  }

  @Test
  void infiniteSequenceTest() {
    final var finiteFlux = Flux.interval(Duration.ofMillis(100))
        .take(3)
        .log();

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .expectNext(0L, 1L, 2L)
        .verifyComplete();
  }

  @Test
  void infiniteSequenceMap() {
    final var finiteFlux = Flux.interval(Duration.ofMillis(100))
        .map(Long::intValue)
        .take(3)
        .log();

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .expectNext(0, 1, 2)
        .verifyComplete();
  }

  @Test
  void infiniteSequenceMap_withDelay() {
    final var finiteFlux = Flux.interval(Duration.ofMillis(100))
        .delayElements(Duration.ofSeconds(1))
        .map(Long::intValue)
        .take(3)
        .log();

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .expectNext(0, 1, 2)
        .verifyComplete();
  }

}
