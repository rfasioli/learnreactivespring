package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    final List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void fluxUsingIterable() {
        final var namesFlux = Flux.fromIterable(names);

        StepVerifier.create(namesFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        final var names = new String[]{"adam", "anna", "jack", "jenny"};
        final var namesFlux = Flux.fromArray(names);

        StepVerifier.create(namesFlux.log())
                .expectNext(names)
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        final var namesFlux = Flux.fromStream(names.stream());

        StepVerifier.create(namesFlux.log())
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void monoUsingJustOrEmpty() {
        final var mono = Mono.justOrEmpty(null);

        StepVerifier.create(mono.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {
        Supplier<String> stringSupplier = () -> "adam";

        final var mono = Mono.fromSupplier(stringSupplier);

        StepVerifier.create(mono.log())
                .expectNext(stringSupplier.get())
                .verifyComplete();
    }

    @Test
    public void fluxUsingRange() {
        final var integerFlux = Flux.range(1, 5);

        StepVerifier.create(integerFlux.log())
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }
}
