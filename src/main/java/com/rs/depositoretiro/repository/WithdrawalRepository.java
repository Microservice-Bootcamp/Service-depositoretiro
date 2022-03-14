package com.rs.depositoretiro.repository;

import com.rs.depositoretiro.entity.Withdrawal;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface WithdrawalRepository extends ReactiveMongoRepository<Withdrawal, String> {
}
