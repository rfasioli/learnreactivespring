package com.rfasioli.learnreactivespring.repository;

import com.rfasioli.learnreactivespring.document.Item;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemReactiveRepositoryTest {

  @Autowired
  ItemReactiveRepository itemReactiveRepository;

  List<Item> itemList = Arrays.asList(
      new Item(null, "Samsung TV", 400.0),
      new Item(null, "LG TV", 420.0),
      new Item(null, "Apple Watch", 299.99),
      new Item(null, "Beats Headphones", 149.99),
      new Item("ABC", "Bose Headphones", 149.99));

  @BeforeAll
  void setup() {
    itemReactiveRepository.deleteAll()
        .thenMany(Flux.fromIterable(itemList))
        .flatMap(itemReactiveRepository::save)
        .doOnNext(item -> System.out.println("Inserted Item is: " + item))
        .blockLast();
  }

  @Test
  void getAllItems() {
    StepVerifier.create(itemReactiveRepository.findAll())
        .expectSubscription()
        .expectNextCount(5)
        .verifyComplete();
  }

  @Test
  void getItemById() {
    StepVerifier.create(itemReactiveRepository.findById("ABC"))
        .expectSubscription()
        .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
        .verifyComplete();
  }

  @Test
  void findItemByDescription() {
    StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphones").log("findItemByDescription: "))
        .expectSubscription()
        .expectNextMatches(item -> item.getId().equals("ABC"))
        .verifyComplete();
  }

  @Test
  void saveItem() {
    final var item = new Item(null, "Google Home Mini", 30.0);
    Mono<Item> savedItem = itemReactiveRepository.save(item);

    StepVerifier.create(savedItem.log())
        .expectSubscription()
        .expectNextMatches(item1 -> item1.getId() != null && item1.getDescription().equals("Google Home Mini"))
        .verifyComplete();
  }

}
