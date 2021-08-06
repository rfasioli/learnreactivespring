package com.rfasioli.learnreactivespring.repository;

import com.rfasioli.learnreactivespring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {

  Flux<Item> findByDescription(String description);

}
