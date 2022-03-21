package com.rs.depositoretiro.controller;

import com.rs.depositoretiro.service.FeesService;
import com.rs.depositoretiro.vo.VoFees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/fees")
public class FeesController {

    @Autowired
    private FeesService feesService;

    @GetMapping("/all/{accountNumber}")
    public Flux<VoFees> getAllFees(@PathVariable("accountNumber") Integer accountNumber){
        return feesService.getAllFeesByAccount(accountNumber);
    }

}
