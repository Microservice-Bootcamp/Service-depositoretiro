package com.rs.depositoretiro.service;

import com.rs.depositoretiro.entity.Fees;
import com.rs.depositoretiro.entity.Withdrawal;
import com.rs.depositoretiro.repository.FeesRepositoty;
import com.rs.depositoretiro.repository.WithdrawalRepository;
import com.rs.depositoretiro.util.WebClientTemplate;
import com.rs.depositoretiro.vo.business.VOBusinessAccount;
import com.rs.depositoretiro.vo.business.VOBusinessMovement;
import com.rs.depositoretiro.vo.personal.VOPersonalBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private WebClientTemplate webClientTemplate;

    @Autowired
    private FeesRepositoty feesRepositoty;

    public Mono<Withdrawal> saveTransaction(Withdrawal withdrawal){
        return getAccount(withdrawal.getAccount())
                .filter(account ->account.getBalance() !=null && withdrawal.getAmount()>0)
                .map(value->{
                    value.setBalance(value.getBalance()-withdrawal.getAmount());
                    return value;
                })
                .flatMap(this::validateFessWithCriteria)
                .then(withdrawalRepository.save(withdrawal));
    }

    public Mono<Withdrawal> saveTransactionOfBusinessAccount(Withdrawal withdrawal){
        return getBusinessAccount(withdrawal.getAccount())
                .filter(account -> account.getBalance() != null && withdrawal.getAmount()>0)
                .map(value->{
                    value.setBalance(value.getBalance()-withdrawal.getAmount());
                    return value;
                })
                .flatMap(this::validateFessWithCriteriaOfBusinessAccount)
                .then(withdrawalRepository.save(withdrawal));

    }

    /*private Mono<Boolean> existBankAccount(Integer accountNumber, String pathUrl, String uriUrl){

        var result = webClientTemplate.templateWebClient(pathUrl)
                .get()
                .uri(uriUrl+accountNumber)
                .retrieve()
                .bodyToMono(ExistDestinationAccountNumber.class);
        return result.flatMap(condition-> (condition.getExitAccount())?Mono.just(true):Mono.just(false));
    }*/

    private Mono<VOPersonalBankAccount> getAccount(Integer accountNumber){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .get()
                .uri("/account/detail/"+accountNumber)
                .retrieve()
                .bodyToMono(VOPersonalBankAccount.class);
    }


    private Mono<VOPersonalBankAccount> sendUpdateTransaction(VOPersonalBankAccount voPersonalBankAccount){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .put()
                .uri("/account/update")
                .body(Mono.just(voPersonalBankAccount), VOPersonalBankAccount.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VOPersonalBankAccount.class);
    }


    private Mono<VOBusinessAccount> getBusinessAccount(Integer accountNumber){
        return webClientTemplate.templateWebClient("http://localhost:8083")
                .get()
                .uri("/account/"+accountNumber)
                .retrieve()
                .bodyToMono(VOBusinessAccount.class);
    }


    private Mono<VOBusinessAccount> sendUpdateTransactionToBusinessAccount(VOBusinessAccount voBusinessAccount){
        return webClientTemplate.templateWebClient("http://localhost:8083")
                .put()
                .uri("/account/save")
                .body(Mono.just(voBusinessAccount), VOBusinessAccount.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VOBusinessAccount.class);
    }

    public Flux<VOBusinessMovement> findAllMovement(Integer accountNumber){
        return withdrawalRepository.findAllByAccount(accountNumber);
    }

    /**
     * validate if transaction number exceeds to movement is charge fee
     * update personal account bank
     * @param voPersonalBankAccount same kind
     * @return update this
     */
    private Mono<VOPersonalBankAccount> validateFessWithCriteria(VOPersonalBankAccount voPersonalBankAccount){
        return withdrawalRepository.countByAccount(voPersonalBankAccount.getAccountNumber())
                .flatMap(condition -> {
                    if(condition>= voPersonalBankAccount.getMovementNumber()){
                        voPersonalBankAccount.setBalance(voPersonalBankAccount.getBalance()-voPersonalBankAccount.getMaintenanceCharge());
                        return feesRepositoty.save(new Fees(voPersonalBankAccount.getAccountNumber(), voPersonalBankAccount.getMaintenanceCharge(), LocalDate.now()))
                                .flatMap(value->sendUpdateTransaction(voPersonalBankAccount));
                    }
                    else {
                        return sendUpdateTransaction(voPersonalBankAccount);
                    }
                });
    }

    private Mono<VOBusinessAccount> validateFessWithCriteriaOfBusinessAccount(VOBusinessAccount voBusinessAccount){
        return withdrawalRepository.countByAccount(voBusinessAccount.getAccountNumber())
                .flatMap(condition -> {
                    if(condition>= voBusinessAccount.getMovementNumber()){
                        voBusinessAccount.setBalance(voBusinessAccount.getBalance()-voBusinessAccount.getMaintenanceCharge());
                        return feesRepositoty.save(new Fees(voBusinessAccount.getAccountNumber(), voBusinessAccount.getMaintenanceCharge(), LocalDate.now()))
                                .flatMap(value->sendUpdateTransactionToBusinessAccount(voBusinessAccount));
                    }
                    else {
                        return sendUpdateTransactionToBusinessAccount(voBusinessAccount);
                    }
                });
    }

}
