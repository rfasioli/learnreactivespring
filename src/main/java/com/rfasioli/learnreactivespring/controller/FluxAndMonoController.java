package com.rfasioli.learnreactivespring.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxAndMonoController {
  @GetMapping("/flux") // Blocking end-point, because produces blocking json
  public Flux<Integer> getFlux() {
    return Flux.just(1, 2, 3, 4)
        .delayElements(Duration.ofSeconds(1))
        .log();
  }

  @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<Integer> getFluxStream() {
    return Flux.just(1, 2, 3, 4)
        .delayElements(Duration.ofSeconds(1))
        .log();
  }

  @GetMapping(value = "/fluxtextstream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Integer> getFluxTextStream() {
    return Flux.just(1, 2, 3, 4)
        .delayElements(Duration.ofSeconds(1))
        .log();
  }
}
