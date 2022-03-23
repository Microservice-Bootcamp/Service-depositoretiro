package com.rs.depositoretiro.controller;

import com.rs.depositoretiro.entity.Deposit;
import com.rs.depositoretiro.service.ReportService;
import com.rs.depositoretiro.vo.resume.ReportGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/resume")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("/account/{account}")
    public Mono<ReportGeneral> getmega(@PathVariable("account") Integer accountNumber){
        return reportService.generalReportByAccount(accountNumber);
    }

    @GetMapping("/general/{account}")
    public Mono<ReportGeneral> generalreport(@PathVariable("account") Integer accountNumber, @RequestParam(name = "start") @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                 @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
        System.out.println( start +""+ end + " jaa" + accountNumber);
        return reportService.getalldate(accountNumber, start, end);
    }


}
