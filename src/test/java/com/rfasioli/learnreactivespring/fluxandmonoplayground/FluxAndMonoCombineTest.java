package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

class FluxAndMonoCombineTest {

  @Test
  void combineUsingMerge() {
    final var flux1 = Flux.just("A", "B", "C");
    final var flux2 = Flux.just("D", "E", "F");

    final var mergeFlux = Flux.merge(flux1, flux2);

    StepVerifier.create(mergeFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  void combineUsingMerge_WithDelay() {
    final var flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(2));
    final var flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(2));

    final var mergeFlux = Flux.merge(flux1, flux2);

    StepVerifier.create(mergeFlux.log())
        .expectSubscription()
        .expectNextCount(6)
        .verifyComplete();
  }

  @Test
  void combineUsingMerge_concat() {
    final var flux1 = Flux.just("A", "B", "C");
    final var flux2 = Flux.just("D", "E", "F");

    final var mergeFlux = Flux.concat(flux1, flux2);

    StepVerifier.create(mergeFlux.log())
        .expectSubscription()
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  void combineUsingMerge_concat_withDelay() {
    VirtualTimeScheduler.getOrSet();

    final var flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));
    final var flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(100));

    final var mergeFlux = Flux.concat(flux1, flux2);

    StepVerifier.withVirtualTime(mergeFlux::log)
        .expectSubscription()
        .expectNext("A", "B", "C", "D", "E", "F")
        .verifyComplete();
  }

  @Test
  void combineUsingZip() {
    final var flux1 = Flux.just("A", "B", "C");
    final var flux2 = Flux.just("D", "E", "F");

    final var mergeFlux = Flux.zip(flux1, flux2);

    StepVerifier.create(mergeFlux.log())
        .expectSubscription()
        .expectNextCount(3)
        .verifyComplete();
  }

  @Test
  void combineUsingZip_andConcat() {
    final var flux1 = Flux.just("A", "B", "C");
    final var flux2 = Flux.just("D", "E", "F");

    final var mergeFlux = Flux.zip(
        flux1,
        flux2,
        (t1, t2) -> t1.concat(t2));

    StepVerifier.create(mergeFlux.log())
        .expectSubscription()
        .expectNext("AD", "BE", "CF")
        .verifyComplete();
  }
}
