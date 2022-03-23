package com.rs.depositoretiro.repository;

import com.rs.depositoretiro.entity.Fees;
import com.rs.depositoretiro.vo.VoFees;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface FeesRepositoty extends ReactiveMongoRepository<Fees, String> {

    Flux<VoFees> findAllByAccount(Integer accountNumber);
    Flux<VoFees> findAllByAccountAndDateBetween(Integer account, LocalDate one, LocalDate two);
}
