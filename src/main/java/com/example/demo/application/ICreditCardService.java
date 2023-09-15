package com.example.demo.application;

import com.example.demo.domain.document.CreditCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditCardService {
    Mono<CreditCard> findCardByNumber(String cardNumber);
    Mono<CreditCard> saveCreditCard(CreditCard creditCard);
    Mono<Void> makePayment(String cardNumber, Double amount);
    Mono<Void> registerPurchase(String cardNumber, Double amount, String description);
    Flux<CreditCard> findCardsByClientId(String clientId);
}
