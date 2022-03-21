package com.rs.depositoretiro.vo.business;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VOBusinessAccount {

    private String idBankAccount;

    private Integer dniUser;
    private Integer accountNumber;
    private Integer balance;
    private String typeAccount;
    private Integer maintenanceCharge;
    private Integer movementNumber;
    private Boolean benefitStatus;

}
