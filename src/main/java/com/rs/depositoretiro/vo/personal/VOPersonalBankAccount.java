package com.rs.depositoretiro.vo.personal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VOPersonalBankAccount {

    private String idBankAccount;

    private String idUser;
    private Integer accountNumber;
    private Integer balance;
    private String typeAccount;
    private Integer maintenanceCharge;
    private Integer movementNumber;

}
