package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {
    final List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap() {
        final var namesFlux = Flux.fromIterable(names)
                .map((n) -> n.toUpperCase());

        StepVerifier.create(namesFlux.log())
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Length_repeat() {
        final var namesFlux = Flux.fromIterable(names)
                .map((n) -> n.length())
                .repeat(1);

        StepVerifier.create(namesFlux.log())
                .expectNext(4,4,4,5,4,4,4,5)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_Filter() {
        final var namesFlux = Flux.fromIterable(names)
                .filter((n) -> n.length() > 4)
                .map((n) -> n.toUpperCase());

        StepVerifier.create(namesFlux.log())
                .expectNext("JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {
        final var stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .flatMap(s -> Flux.fromIterable(convertToList(s)));

        StepVerifier.create(stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "newValue");
    }

    @Test
    public void transformUsingFlatMapUsingParallel() {
        final var stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)
                .flatMap((n) -> n.map(
                                this::convertToList)
                                .subscribeOn(parallel())
                                .flatMap((s) -> Flux.fromIterable(s))
                );

        StepVerifier.create(stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMapUsingParallel_MantainingOrder() {
        final var stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D","E","F"))
                .window(2)
                .flatMapSequential((n) -> n.map(
                        this::convertToList)
                        .subscribeOn(parallel())
                        .flatMap((s) -> Flux.fromIterable(s))
                );

        StepVerifier.create(stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }
}
