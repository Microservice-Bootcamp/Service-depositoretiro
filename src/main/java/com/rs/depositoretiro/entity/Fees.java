package com.rs.depositoretiro.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "fees")
public class Fees {
    @Id
    private String idFee;

    private Integer account;
    private Integer amount;


    private LocalDate date;

    public Fees(Integer account, Integer amount, LocalDate date) {
        this.account = account;
        this.amount = amount;
        this.date = date;
    }
}
