package com.rs.depositoretiro.repository;

import com.rs.depositoretiro.entity.Withdrawal;
import com.rs.depositoretiro.vo.business.VOBusinessMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WithdrawalRepository extends ReactiveMongoRepository<Withdrawal, String> {
    Flux<VOBusinessMovement> findAllByAccount(Integer accountNumber);
    Mono<Long> countByAccount(Integer accountNumber);
}
