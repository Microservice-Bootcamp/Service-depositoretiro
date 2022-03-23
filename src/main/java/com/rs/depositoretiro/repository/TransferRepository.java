package com.rs.depositoretiro.repository;

import com.rs.depositoretiro.entity.Transfer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface TransferRepository extends ReactiveMongoRepository<Transfer, String> {

    Flux<Transfer> findAllByDestinationAccount(Integer accountNumber);
    Flux<Transfer> findAllByDestinationAccountAndDateBetween(Integer account, LocalDate one, LocalDate two);
}
