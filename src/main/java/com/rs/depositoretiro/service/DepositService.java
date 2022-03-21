package com.rs.depositoretiro.service;

import com.rs.depositoretiro.entity.Deposit;
import com.rs.depositoretiro.entity.Fees;
import com.rs.depositoretiro.repository.DepositRepository;
import com.rs.depositoretiro.repository.FeesRepositoty;
import com.rs.depositoretiro.util.WebClientTemplate;
import com.rs.depositoretiro.vo.ExistDestinationAccountNumber;
import com.rs.depositoretiro.vo.ExistIssuerAccountNumber;
import com.rs.depositoretiro.vo.business.VOBusinessAccount;
import com.rs.depositoretiro.vo.personal.VOPersonalBankAccount;
import com.rs.depositoretiro.vo.personal.VOPersonalMovement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private FeesRepositoty feesRepositoty;

    @Autowired
    private WebClientTemplate webClientTemplate;

    public Mono<Deposit> saveTransactionOfPersonalAccount(Deposit deposit){
        return getPersonalAccount(deposit.getDestinationAccount())
                .filter(account ->account.getBalance() !=null && deposit.getAmount()>0)
                .map(value ->{
                    value.setBalance(value.getBalance()+deposit.getAmount());
                    return value;
                })
                .flatMap(this::validateFessWithCriteria)
                .then(depositRepository.save(deposit));
    }

    public Mono<Deposit> saveTransactionofBuinessAccount(Deposit deposit){
        return getBusinessAccount(deposit.getDestinationAccount())
                .filter(account ->account.getBalance() !=null && deposit.getAmount()>0)
                .map(value -> {
                    value.setBalance(value.getBalance() + deposit.getAmount());
                    return value;
                })
                .flatMap(this::validateFessWithCriteriaOfBusiness)
                .then(depositRepository.save(deposit));
    }


    private Mono<VOPersonalBankAccount> getPersonalAccount(Integer accountNumber){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .get()
                .uri("/account/detail/"+accountNumber)
                .retrieve()
                .bodyToMono(VOPersonalBankAccount.class);
    }

    private Mono<VOPersonalBankAccount> updatePersonalBalance(VOPersonalBankAccount voPersonalBankAccount){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .put()
                .uri("/account/update")
                .body( Mono.just(voPersonalBankAccount), VOPersonalBankAccount.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VOPersonalBankAccount.class);
    }

    private Mono<VOBusinessAccount> updateBusinessBalance(VOBusinessAccount voBusinessAccount){
        return webClientTemplate.templateWebClient("http://localhost:8083")
                .put()
                .uri("/account/save")
                .body( Mono.just(voBusinessAccount), VOBusinessAccount.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VOBusinessAccount.class);
    }
    private Mono<VOBusinessAccount> getBusinessAccount(Integer accountNumber){
        return webClientTemplate.templateWebClient("http://localhost:8083")
                .get()
                .uri("/account/"+accountNumber)
                .retrieve()
                .bodyToMono(VOBusinessAccount.class);
    }

    /**
     * validate if transaction number exceeds to movement is charge fee
     * update personal account bank
     * @param voPersonalBankAccount same kind
     * @return update this
     */
    private Mono<VOPersonalBankAccount> validateFessWithCriteria(VOPersonalBankAccount voPersonalBankAccount){
        return depositRepository.countByDestinationAccount(voPersonalBankAccount.getAccountNumber())
                .flatMap(condition -> {
                    if(condition>= voPersonalBankAccount.getMovementNumber()){
                        voPersonalBankAccount.setBalance(voPersonalBankAccount.getBalance()-voPersonalBankAccount.getMaintenanceCharge());
                        return feesRepositoty.save(new Fees(voPersonalBankAccount.getAccountNumber(), voPersonalBankAccount.getMaintenanceCharge(), LocalDate.now()))
                                .flatMap(value->updatePersonalBalance(voPersonalBankAccount));
                    }
                    else {
                        return updatePersonalBalance(voPersonalBankAccount);
                    }

                });
    }

    private Mono<VOBusinessAccount> validateFessWithCriteriaOfBusiness(VOBusinessAccount voBusinessAccount){
        return depositRepository.countByDestinationAccount(voBusinessAccount.getAccountNumber())
                .flatMap(condition -> {
                    if(condition>= voBusinessAccount.getMovementNumber()){
                        voBusinessAccount.setBalance(voBusinessAccount.getBalance()-voBusinessAccount.getMaintenanceCharge());
                        return feesRepositoty.save(new Fees(voBusinessAccount.getAccountNumber(), voBusinessAccount.getMaintenanceCharge(), LocalDate.now()))
                                .flatMap(value->updateBusinessBalance(voBusinessAccount));
                    }
                    else {
                        return updateBusinessBalance(voBusinessAccount);
                    }

                });
    }


    public Flux<VOPersonalMovement> findAllPersonalMovement(Integer accountNumber){
        return depositRepository.findAllByDestinationAccount(accountNumber)
                .filter(value->value.getAmount() !=null)
                .flatMap(movement -> Flux.just(new VOPersonalMovement(movement.getDestinationAccount(),movement.getAmount(), movement.getDate())));
    }


}
