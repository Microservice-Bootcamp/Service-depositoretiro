package com.rs.depositoretiro.service;

import com.rs.depositoretiro.entity.Withdrawal;
import com.rs.depositoretiro.repository.WithdrawalRepository;
import com.rs.depositoretiro.util.WebClientTemplate;
import com.rs.depositoretiro.vo.ExistDestinationAccountNumber;
import com.rs.depositoretiro.vo.business.VOBusinessAccount;
import com.rs.depositoretiro.vo.business.VOBusinessMovement;
import com.rs.depositoretiro.vo.personal.VOPersonalBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private WebClientTemplate webClientTemplate;

    public Mono<Withdrawal> saveTransaction(Withdrawal withdrawal){
        return existBankAccount(withdrawal.getAccount(),
                "http://localhost:8081",
                "/account/"
        )
                .flatMap(condition->{
                    if(condition.equals(true)){
                        return updateBalanceInMemory(withdrawal)
                                .flatMap(this::sendUpdateTransaction)
                                .then(withdrawalRepository.save(withdrawal));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Withdrawal> saveTransactionOfBusinessAccount(Withdrawal withdrawal){
        return existBankAccount(
                withdrawal.getAccount(),
                "http://localhost:8083",
                "/account/status/"
        )
                .flatMap(condition ->{
                    if(condition.equals(true)){
                        return updateBusinessBalanceInMemory(withdrawal)
                                .flatMap(this::sendUpdateTransactionToBusinessAccount)
                                .then(withdrawalRepository.save(withdrawal));
                    }
                    return Mono.empty();
                });
    }


    private Mono<Boolean> existBankAccount(Integer accountNumber, String pathUrl, String uriUrl){

        var result = webClientTemplate.templateWebClient(pathUrl)
                .get()
                .uri(uriUrl+accountNumber)
                .retrieve()
                .bodyToMono(ExistDestinationAccountNumber.class);
        return result.flatMap(condition-> (condition.getExitAccount())?Mono.just(true):Mono.just(false));
    }

    private Mono<VOPersonalBankAccount> getAccount(Integer accountNumber){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .get()
                .uri("/account/detail/"+accountNumber)
                .retrieve()
                .bodyToMono(VOPersonalBankAccount.class);
    }

    private Mono<VOPersonalBankAccount> updateBalanceInMemory(Withdrawal withdrawal){
        return getAccount(withdrawal.getAccount())
                .map(value->{
                    if(withdrawal.getAmount()<value.getBalance() && withdrawal.getAmount()>0){
                        value.setBalance(value.getBalance()-withdrawal.getAmount());
                        return value;
                    }
                    withdrawal.setAmount(0);
                    return value;
                });
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

    private Mono<VOBusinessAccount> updateBusinessBalanceInMemory(Withdrawal withdrawal){
        return getBusinessAccount(withdrawal.getAccount())
                .map(value->{
                    if(withdrawal.getAmount()<value.getBalance() && withdrawal.getAmount()>0){
                        value.setBalance(value.getBalance()-withdrawal.getAmount());
                        return value;
                    }
                    withdrawal.setAmount(0);
                    return value;
                });
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

}
