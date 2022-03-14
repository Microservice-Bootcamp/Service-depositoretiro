package com.rs.depositoretiro.vo.business;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VOBusinessAccount {

    private String idBankAccount;

    private List<String> idUser;
    private Integer accountNumber;
    private Integer balance;
    private String typeAccount;
    private Integer maintenanceCharge;
    private Integer movementNumber;

}
