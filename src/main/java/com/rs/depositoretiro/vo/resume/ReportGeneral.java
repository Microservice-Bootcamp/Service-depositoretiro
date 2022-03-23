package com.rs.depositoretiro.vo.resume;

import com.rs.depositoretiro.entity.Deposit;
import com.rs.depositoretiro.entity.Fees;
import com.rs.depositoretiro.entity.Transfer;
import com.rs.depositoretiro.entity.Withdrawal;
import com.rs.depositoretiro.vo.VoFees;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportGeneral {

    private Integer accountNumber;
    private List<Deposit> deposits;
    private List<VoFees> fees;
    private List<Transfer> transfers;
    private List<Withdrawal> withdrawals;
}
