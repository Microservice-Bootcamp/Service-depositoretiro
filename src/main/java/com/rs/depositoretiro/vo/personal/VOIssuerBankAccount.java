package com.rs.depositoretiro.vo.personal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VOIssuerBankAccount {

    private String idBankAccount;

    private Integer dniUser;
    private Integer accountNumber;
    private Integer balance;
    private String typeAccount;
    private Integer maintenanceCharge;
    private Integer movementNumber;
    private Boolean benefitStatus;
}
