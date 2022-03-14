package com.rs.depositoretiro.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "deposit")
public class Deposit {
    @Id
    private String idDeposit;

    private Integer destinationAccount;
    private Integer issuerAccount;
    private Integer amount;

}
