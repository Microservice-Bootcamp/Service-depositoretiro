package com.rs.depositoretiro.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "withdrawal")
public class Withdrawal {

    @Id
    private String idRetiro;

    private Integer account;
    private Integer amount;

    @Builder.Default
    private LocalDate date = LocalDate.now();

}
