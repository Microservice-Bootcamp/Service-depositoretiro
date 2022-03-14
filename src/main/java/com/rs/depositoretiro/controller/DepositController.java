package com.rs.depositoretiro.controller;

import com.rs.depositoretiro.entity.Deposit;
import com.rs.depositoretiro.service.DepositService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/deposit")
public class DepositController {

    @Autowired
    private DepositService depositService;


    @PostMapping("/send")
    public Mono<Deposit> saveTransaction(@RequestBody Deposit deposit){
        return depositService.saveTransactionOfPersonalAccount(deposit);
    }

    @PutMapping("/update")
    public Mono<Deposit> updateTransaction(@RequestBody Deposit deposit){
        return depositService.saveTransactionOfPersonalAccount(deposit);
    }
    @PostMapping("/business/send")
    public Mono<Deposit> saveBusinessTransaction(@RequestBody Deposit deposit){
        return depositService.saveTransactionofBuinessAccount(deposit);
    }
}
