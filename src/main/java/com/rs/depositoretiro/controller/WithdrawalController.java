package com.rs.depositoretiro.controller;

import com.rs.depositoretiro.entity.Withdrawal;
import com.rs.depositoretiro.service.WithdrawalService;
import com.rs.depositoretiro.vo.business.VOBusinessMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/withdrawal")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @PostMapping("/personal/send")
    public Mono<ResponseEntity<Withdrawal>> saveTransaction(@RequestBody Withdrawal withdrawal){
        return withdrawalService.saveTransaction(withdrawal)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/business/send")
    public Mono<ResponseEntity<Withdrawal>> saveBusinessTransaction(@RequestBody Withdrawal withdrawal){
        return withdrawalService.saveTransactionOfBusinessAccount(withdrawal)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());

    }

    @GetMapping("/movement/{account}")
    public Flux<VOBusinessMovement> findAllMovement(@PathVariable("account") Integer accountNumber){
        return withdrawalService.findAllMovement(accountNumber);
    }

}
