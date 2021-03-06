package com.rs.depositoretiro.vo.personal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class VOPersonalMovement {
    private Integer issuerAccount;
    private Integer amount;
    private LocalDate date;
}
