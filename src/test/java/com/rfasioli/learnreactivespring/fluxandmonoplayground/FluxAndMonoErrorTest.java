package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import com.rfasioli.learnreactivespring.fluxandmonoplayground.exception.CustomException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FluxAndMonoErrorTest {
    @Test
    void fluxErrorHandling() {
        final var stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void fluxErrorHandling_onErrorResume() {
        final var stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e) -> {
                    System.err.println("Exception is: " + e);
                    return Flux.just("default", "default1");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default", "default1")
                .verifyComplete();
    }

    @Test
    void fluxErrorHandling_onErrorReturn() {
        final var stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void fluxErrorHandling_onErrorMap() {
        final var stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException());

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    void fluxErrorHandling_onErrorMap_withRetry() {
        final var stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException())
                .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

}
