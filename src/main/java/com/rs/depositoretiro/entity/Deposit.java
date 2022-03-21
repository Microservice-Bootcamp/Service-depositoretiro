package com.rs.depositoretiro.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "deposit")
public class Deposit {
    @Id
    private String idDeposit;

    private Integer destinationAccount;
    private Integer issuerDni;
    private Integer amount;

    @Builder.Default
    private LocalDate date = LocalDate.now();

}
