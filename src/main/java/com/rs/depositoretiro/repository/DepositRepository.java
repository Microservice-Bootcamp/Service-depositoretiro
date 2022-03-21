package com.rs.depositoretiro.repository;

import com.rs.depositoretiro.entity.Deposit;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DepositRepository  extends ReactiveMongoRepository<Deposit, String> {

    Mono<Boolean> existsByDestinationAccount(Integer destinationAccount);
    //Mono<Boolean> existsByIssuerAccount(Integer issuerAcount);
    Flux<Deposit> findAllByDestinationAccount(Integer accounNumber);
    Mono<Long> countByDestinationAccount(Integer accountNumber);
}
