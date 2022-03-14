package com.rs.depositoretiro.service;

import com.rs.depositoretiro.entity.Deposit;
import com.rs.depositoretiro.repository.DepositRepository;
import com.rs.depositoretiro.util.WebClientTemplate;
import com.rs.depositoretiro.vo.ExistDestinationAccountNumber;
import com.rs.depositoretiro.vo.ExistIssuerAccountNumber;
import com.rs.depositoretiro.vo.business.VOBusinessAccount;
import com.rs.depositoretiro.vo.personal.VOPersonalBankAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private WebClientTemplate webClientTemplate;

    public Mono<Deposit> saveTransactionOfPersonalAccount(Deposit deposit){
        if(deposit.getAmount()>0) {
            return exitPersonalAccountOfIssuerAndDestintion(
                    deposit.getIssuerAccount(), deposit.getDestinationAccount(),
                    "http://localhost:8081",
                    "/account/"
            )
                    .flatMap(condition -> {
                       if (condition.equals(true)) {
                            return updatePersonalBalanceInMemory(deposit)
                                    .flatMap(this::updatePersonalBalance)
                                    .then(depositRepository.save(deposit));
                        }
                        return Mono.empty();
                  });
        }
        return Mono.empty();
    }
    public Mono<Deposit> saveTransactionofBuinessAccount(Deposit deposit){
        if(deposit.getAmount()>0){
            return exitPersonalAccountOfIssuerAndDestintion(
                    deposit.getIssuerAccount(),
                    deposit.getDestinationAccount(),
                    "http://localhost:8083",
                    "/account/status/"
            )
                    .flatMap(condition ->{
                        if(condition.equals(true)){
                            return updateBusinessBalanceInMemory(deposit)
                                    .flatMap(this::updateBusinessBalance)
                                    .then(depositRepository.save(deposit));
                        }
                       return Mono.empty();

                    });
        }
        return Mono.empty();
    }


    private Mono<VOPersonalBankAccount> getPersonalAccount(Integer accountNumber){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .get()
                .uri("/account/detail/"+accountNumber)
                .retrieve()
                .bodyToMono(VOPersonalBankAccount.class);
    }
    private Mono<VOPersonalBankAccount> updatePersonalBalanceInMemory(Deposit deposit){
        return getPersonalAccount(deposit.getDestinationAccount())
                .map(value->{
                    value.setBalance(value.getBalance()+deposit.getAmount());
                    return value;
                });
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

    private Mono<VOBusinessAccount> updateBusinessBalanceInMemory(Deposit deposit){
        return getBusinessAccount(deposit.getDestinationAccount())
                .map(value->{
                    value.setBalance(value.getBalance()+deposit.getAmount());
                    return value;
                });
    }


    private Mono<Boolean> exitPersonalAccountOfIssuerAndDestintion(Integer issuerAccountNumber, Integer destinationAccountNumber,String pathUrl, String uri){

        var issuerResult = webClientTemplate.templateWebClient(pathUrl).get()
                .uri(uri+issuerAccountNumber)
                .retrieve()
                .bodyToMono(ExistIssuerAccountNumber.class);

        var destinationResult = webClientTemplate.templateWebClient(pathUrl).get()
                .uri(uri+destinationAccountNumber)
                .retrieve()
                .bodyToMono(ExistDestinationAccountNumber.class);

        return issuerResult.flatMap(issuer-> destinationResult.flatMap(destination->{
            if(destination.getExitAccount()){
                return Mono.just(true);
            }
            return Mono.just(false);
        }));

    }




}
