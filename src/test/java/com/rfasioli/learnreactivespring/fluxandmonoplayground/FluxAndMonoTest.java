package com.rfasioli.learnreactivespring.fluxandmonoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {
    @Test
    public void fluxTest() {
        final var stringFlux = Flux.just("Spring", "Spring Boot", "Reactor Spring")
                .log();

        stringFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println("Exception is: " + e),
                        () -> System.out.println("Completed!"));
    }

    @Test
    public void fluxTestWithError1() {
        final var stringFlux = Flux.just("Spring", "Spring Boot", "Reactor Spring")
                .concatWith(Flux.error(new RuntimeException("Exception ocurred")))
                .concatWith(Flux.just("After error"))
                .log();

        stringFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println("Exception is: " + e),
                        () -> System.out.println("Completed!"));
    }

    @Test
    public void fluxTestWithoutError() {
        final var stringFlux = Flux.just("Spring", "Spring Boot", "Reactor Spring")
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactor Spring")
                .verifyComplete();

    }

    @Test
    public void fluxTestWithError2() {
        final var stringFlux = Flux.just("Spring", "Spring Boot", "Reactor Spring")
                .concatWith(Flux.error(new RuntimeException("Exception ocurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
                .expectNext("Reactor Spring")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fluxTestWithError3() {
        final var stringFlux = Flux.just("Spring", "Spring Boot", "Reactor Spring")
                .concatWith(Flux.error(new RuntimeException("Exception ocurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring", "Spring Boot", "Reactor Spring")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void fluxTestCountWithError() {
        final var stringFlux = Flux.just("Spring", "Spring Boot", "Reactor Spring")
                .concatWith(Flux.error(new RuntimeException("Exception ocurred")))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(3)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void monoTest() {
        final var stringMono = Mono.just("Spring");

        StepVerifier.create(stringMono.log())
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void monoTest_Error() {
        StepVerifier.create(Mono.error(new RuntimeException("Exception ocurred")).log())
                .expectError(RuntimeException.class)
                .verify();
    }
}
