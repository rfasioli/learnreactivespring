package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FluxAndMonoBackPressureTest {

  @Test
  void backPressureTest() {

    Flux<Integer> finiteFlux = Flux.range(1, 10)
        .log();

    StepVerifier.create(finiteFlux)
        .expectSubscription()
        .thenRequest(1)
        .expectNext(1)
        .thenRequest(1)
        .expectNext(2)
        .thenCancel()
        .verify();

  }

  @Test
  void backPressure() {

    Flux<Integer> finiteFlux = Flux.range(1, 10)
        .log();

    finiteFlux.subscribe((element) -> System.out.println("Element is : " + element),
        (e) -> System.err.println("Exception is : " + e),
        () -> System.out.println("Done"),
        (subscription -> subscription.request(2)));

    assertTrue(true);
  }

  @Test
  void backPressure_cancel() {

    Flux<Integer> finiteFlux = Flux.range(1, 10)
        .log();

    finiteFlux.subscribe((element) -> System.out.println("Element is : " + element),
        (e) -> System.err.println("Exception is : " + e),
        () -> System.out.println("Done"),
        (Subscription::cancel));

    assertTrue(true);
  }

  @Test
  void customized_backPressure() {

    Flux<Integer> finiteFlux = Flux.range(1, 10)
        .log();

    finiteFlux.subscribe(new BaseSubscriber<Integer>() {
      @Override
      protected void hookOnNext(Integer value) {
        request(1);
        System.out.println("Value received is : " + value);
        if (value == 4) {
          cancel();
        }
      }
    });

    assertTrue(true);
  }

}
