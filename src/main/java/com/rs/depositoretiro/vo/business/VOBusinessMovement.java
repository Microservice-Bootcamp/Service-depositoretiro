package com.rs.depositoretiro.vo.business;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class VOBusinessMovement {

    private Integer amount;
    private LocalDate date;
}
