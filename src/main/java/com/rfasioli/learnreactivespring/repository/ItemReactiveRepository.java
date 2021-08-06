package com.rfasioli.learnreactivespring.repository;

import com.rfasioli.learnreactivespring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
}
