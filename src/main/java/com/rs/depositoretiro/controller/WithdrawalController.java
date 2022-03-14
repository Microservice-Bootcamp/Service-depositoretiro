package com.rs.depositoretiro.controller;

import com.rs.depositoretiro.entity.Withdrawal;
import com.rs.depositoretiro.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/withdrawal")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @PostMapping("/personal/send")
    public Mono<Withdrawal> saveTransaction(@RequestBody Withdrawal withdrawal){
        return withdrawalService.saveTransaction(withdrawal);
    }

    @PostMapping("/business/send")
    public Mono<Withdrawal> saveBusinessTransaction(@RequestBody Withdrawal withdrawal){
        return withdrawalService.saveTransactionOfBusinessAccount(withdrawal);
    }

}
