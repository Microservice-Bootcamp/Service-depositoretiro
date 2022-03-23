package com.rs.depositoretiro.service;

import com.rs.depositoretiro.entity.Deposit;
import com.rs.depositoretiro.repository.DepositRepository;
import com.rs.depositoretiro.repository.FeesRepositoty;
import com.rs.depositoretiro.repository.TransferRepository;
import com.rs.depositoretiro.repository.WithdrawalRepository;
import com.rs.depositoretiro.vo.resume.ReportGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ReportService {

    @Autowired
    DepositRepository depositRepository;

    @Autowired
    FeesRepositoty feesRepositoty;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    WithdrawalRepository withdrawalRepository;

    public Mono<ReportGeneral>  generalReportByAccount (Integer accountNumber){
        var deposit = depositRepository.findAllByDestinationAccount(accountNumber)
                .collectList();
        var fees = feesRepositoty.findAllByAccount(accountNumber).collectList();
        var transfer = transferRepository.findAllByDestinationAccount(accountNumber).collectList();
        var withdrawall = withdrawalRepository.findAllByAccount(accountNumber,"demo").collectList();

        return deposit.flatMap(deposits -> fees.flatMap(fee -> transfer.flatMap(transfers-> withdrawall.flatMap(with-> Mono.just(new ReportGeneral(accountNumber, deposits, fee, transfers,with))))));
    }

    public Mono<ReportGeneral> getalldate(Integer accountNumber, LocalDate start, LocalDate end){
        var deposit = depositRepository.findAllByDestinationAccountAndDateBetween(accountNumber, start, end).collectList();
        var fees = feesRepositoty.findAllByAccountAndDateBetween(accountNumber, start, end).collectList();
        var transfer = transferRepository.findAllByDestinationAccountAndDateBetween(accountNumber, start, end).collectList();
        var withdrawall = withdrawalRepository.findAllByAccountAndDateBetween(accountNumber, start, end).collectList();

        return deposit.flatMap(deposits -> fees.flatMap(fee -> transfer.flatMap(transfers-> withdrawall.flatMap(with-> Mono.just(new ReportGeneral(accountNumber, deposits, fee, transfers,with))))));
    }

}
