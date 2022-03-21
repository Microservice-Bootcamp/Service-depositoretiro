package com.rs.depositoretiro.controller;

import com.rs.depositoretiro.entity.Transfer;
import com.rs.depositoretiro.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/send")
    public Mono<Transfer> sendTransfer(@RequestBody Transfer transfer){
        return transferService.transferBalance(transfer);
    }
}
