package com.rs.depositoretiro.repository;

import com.rs.depositoretiro.entity.Transfer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransferRepository extends ReactiveMongoRepository<Transfer, String> {
}
