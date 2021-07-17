package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge() {
        final var flux1 = Flux.just("A","B","C");
        final var flux2 = Flux.just("D","E","F");

        final var mergeFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_WithDelay() {
        final var flux1 = Flux.just("A","B","C").delayElements(Duration.ofMillis(2));
        final var flux2 = Flux.just("D","E","F").delayElements(Duration.ofMillis(2));

        final var mergeFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_concat() {
        final var flux1 = Flux.just("A","B","C");
        final var flux2 = Flux.just("D","E","F");

        final var mergeFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_concat_withDelay() {
        final var flux1 = Flux.just("A","B","C").delayElements(Duration.ofMillis(100));
        final var flux2 = Flux.just("D","E","F").delayElements(Duration.ofMillis(100));

        final var mergeFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip() {
        final var flux1 = Flux.just("A","B","C");
        final var flux2 = Flux.just("D","E","F");

        final var mergeFlux = Flux.zip(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void combineUsingZip_andConcat() {
        final var flux1 = Flux.just("A","B","C");
        final var flux2 = Flux.just("D","E","F");

        final var mergeFlux = Flux.zip(
                flux1,
                flux2,
                (t1, t2) -> t1.concat(t2));

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }
}
