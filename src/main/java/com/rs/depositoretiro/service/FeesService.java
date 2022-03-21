package com.rs.depositoretiro.service;

import com.rs.depositoretiro.repository.FeesRepositoty;
import com.rs.depositoretiro.vo.VoFees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class FeesService {

    @Autowired
    private FeesRepositoty feesRepositoty;

    public Flux<VoFees> getAllFeesByAccount(Integer accountNumber){
        return feesRepositoty.findAllByAccount(accountNumber);
    }

}
