package com.rs.depositoretiro.service;

import com.rs.depositoretiro.entity.Transfer;
import com.rs.depositoretiro.repository.TransferRepository;
import com.rs.depositoretiro.util.WebClientTemplate;

import com.rs.depositoretiro.vo.personal.VOIssuerBankAccount;
import com.rs.depositoretiro.vo.personal.VOPersonalBankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private WebClientTemplate webClientTemplate;

    public Mono<Transfer> transferBalance(Transfer transfer){
        return getDestinationAccount(transfer.getDestinationAccount())
                .filter(account -> account.getBalance() !=null && transfer.getAmount()>0)
                .map(value -> {
                    value.setBalance(value.getBalance() + transfer.getAmount());
                    return value;
                })
                .flatMap(updated ->validateFessWithCriteria(updated,transfer))
                .then(transferRepository.save(transfer));


    }

    private Mono<VOPersonalBankAccount> getDestinationAccount(Integer accountNumber){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .get()
                .uri("/account/detail/"+accountNumber)
                .retrieve()
                .bodyToMono(VOPersonalBankAccount.class);
    }

    private Mono<VOIssuerBankAccount> getIssuerAccount(Integer accountNumber){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .get()
                .uri("/account/detail/"+accountNumber)
                .retrieve()
                .bodyToMono(VOIssuerBankAccount.class);
    }

    private Mono<VOPersonalBankAccount> updateDestinationAccount(VOPersonalBankAccount voPersonalBankAccount){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .put()
                .uri("/account/update")
                .body( Mono.just(voPersonalBankAccount), VOPersonalBankAccount.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VOPersonalBankAccount.class);
    }

    private Mono<VOIssuerBankAccount> updateIssuerAccount(VOIssuerBankAccount voIssuerBankAccount){
        return webClientTemplate.templateWebClient("http://localhost:8081")
                .put()
                .uri("/account/update")
                .body( Mono.just(voIssuerBankAccount), VOIssuerBankAccount.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VOIssuerBankAccount.class);
    }

    /**
     * verify if both accounts exist
     * if exist, destination account plus transfer mount and minus of issuer account
     * @param voPersonalBankAccount entity of destination account
     * @param transfer details of transfer entity
     * @return if not exist both Accounts return null;
     */
    private Mono<VOPersonalBankAccount> validateFessWithCriteria(VOPersonalBankAccount voPersonalBankAccount, Transfer transfer){
        return getIssuerAccount(transfer.getIssuerAccount())
                .flatMap(issuer->{
                    if(issuer.getBalance() !=null && issuer.getBalance()>= transfer.getAmount()){
                        issuer.setBalance(issuer.getBalance() - transfer.getAmount());
                        return updateIssuerAccount(issuer)
                                .then(updateDestinationAccount(voPersonalBankAccount));
                    }
                    transfer.setAmount(0);
                    return Mono.empty();
                });
    }
}
