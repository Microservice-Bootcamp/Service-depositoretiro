package com.rs.depositoretiro.controller;

import com.rs.depositoretiro.entity.Deposit;
import com.rs.depositoretiro.service.DepositService;

import com.rs.depositoretiro.vo.personal.VOPersonalBankAccount;
import com.rs.depositoretiro.vo.personal.VOPersonalMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/deposit")
public class DepositController {

    @Autowired
    private DepositService depositService;


    @PostMapping("/send")
    public Mono<ResponseEntity<Deposit>> saveTransaction(@RequestBody Deposit deposit){
        return depositService.saveTransactionOfPersonalAccount(deposit)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<Deposit>> updateTransaction(@RequestBody Deposit deposit){
        return depositService.saveTransactionOfPersonalAccount(deposit)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
    @PostMapping("/business/send")
    public Mono<ResponseEntity<Deposit>> saveBusinessTransaction(@RequestBody Deposit deposit){
        return depositService.saveTransactionofBuinessAccount(deposit)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/movement/{account}")
    public ResponseEntity<Flux<VOPersonalMovement>> findAllMovement(@PathVariable("account") Integer accountNumber){
        return new ResponseEntity<>(depositService.findAllPersonalMovement(accountNumber), HttpStatus.OK);
    }
}
