package com.rfasioli.learnreactivespring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {
  @GetMapping("/flux") // Blocking end-point, because produces blocking json
  public Flux<Integer> getFlux() {
    return Flux.just(1, 2, 3, 4)
        .delayElements(Duration.ofNanos(1))
        .log();
  }

  @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<Integer> getFluxStream() {
    return Flux.just(1, 2, 3, 4)
        .delayElements(Duration.ofNanos(1))
        .log();
  }

  @GetMapping(value = "/fluxtextstream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Integer> getFluxTextStream() {
    return Flux.just(1, 2, 3, 4)
        .delayElements(Duration.ofNanos(1))
        .log();
  }

  @GetMapping(value = "/fluxstreaminfinity", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<Long> getFluxStreamInfinity() {
    return Flux.interval(Duration.ofSeconds(1))
        .log();
  }

  @GetMapping("/mono")
  public Mono<Integer> returnMono() {
    return Mono.just(1)
        .log();
  }
}
