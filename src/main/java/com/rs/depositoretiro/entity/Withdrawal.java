package com.rs.depositoretiro.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "withdrawal")
public class Withdrawal {

    @Id
    private String idRetiro;

    private Integer account;
    private Integer amount;

}
