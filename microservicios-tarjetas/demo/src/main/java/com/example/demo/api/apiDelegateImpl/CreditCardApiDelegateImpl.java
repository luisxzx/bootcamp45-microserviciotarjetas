package com.example.demo.api.apiDelegateImpl;

import com.example.demo.application.ICreditCardService;
import com.example.demo.domain.document.CreditCard;
import com.example.demo.domain.Service.CreditCardService;
import com.example.demo.api.CreditCardsApiDelegate;
import com.example.demo.common.mapper.CreditCardMapper;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CreditCardApiDelegateImpl implements CreditCardsApiDelegate {
    /**
     * Para acceder a CreditCardService.
     */

    @Autowired
    private ICreditCardService creditCardService;

    /**
     * CreditCardApiDelegateImpl.
     * @param cardNumber variable.
     * @return CreditCardBalance.
     */
    @Override
    public Mono<ResponseEntity<CreditCardBalance>> creditCardsCardNumberBalanceGet(String cardNumber, ServerWebExchange exchange) {
        return creditCardService.findCardByNumber(cardNumber)
                .map(CreditCardMapper::toCreditCardBalance)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * CreditCardApiDelegateImpl.
     * @param cardNumber variable.
     * @param creditCardPayment objeto.
     * @return ResponseEntity.
     */
    @Override
    public Mono<ResponseEntity<Void>> creditCardsCardNumberPaymentPost(String cardNumber, Mono<CreditCardPayment> creditCardPayment, ServerWebExchange exchange) {
        return creditCardPayment.flatMap(payment ->
                creditCardService.makePayment(cardNumber, payment.getAmount().doubleValue())
                        .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                        .onErrorResume(e -> Mono.just(new ResponseEntity<Void>(HttpStatus.BAD_REQUEST)))
        );
    }


    /**
     * CreditCardApiDelegateImpl.
     * @param cardNumber variable.
     * @param creditCardPurchase objeto.
     * @return ResponseEntity.
     */
    @Override
    public Mono<ResponseEntity<Void>> creditCardsCardNumberPurchasePost(String cardNumber, Mono<CreditCardPurchase> creditCardPurchase, ServerWebExchange exchange) {
        return creditCardPurchase.flatMap(purchase ->
                creditCardService.registerPurchase(cardNumber, purchase.getAmount().doubleValue(),purchase.getDescription())
                        .onErrorResume(e -> {
                            System.err.println("Error: " + e.getMessage());
                            return Mono.empty();
                        })
                        .then(Mono.just(ResponseEntity.<Void>ok().build()))
        );
    }



    /**
     * CreditCardApiDelegateImpl.
     * @param creditCardCreateInput objeto.
     * @return CreditCardDetails.
     */
    @Override
    public Mono<ResponseEntity<CreditCardDetails>> creditCardsPost(Mono<CreditCardCreateInput> creditCardCreateInput, ServerWebExchange exchange) {
        return creditCardCreateInput.flatMap(input -> {
            CreditCard creditCard = CreditCardMapper.fromCreditCardCreateInput(input);
            return creditCardService.saveCreditCard(creditCard)
                    .map(CreditCardMapper::toCreditCardDetails)
                    .map(ResponseEntity::ok);
        });
    }

    /**
     * CreditCardApiDelegateImpl.
     * @param clientId variable.
     * @return CreditCardDetails lista.
     */
    @Override
    public Mono<ResponseEntity<Flux<CreditCardDetails>>> creditCardsByClientClientIdGet(String clientId, ServerWebExchange exchange) {
        Flux<CreditCardDetails> creditCardDetailsFlux = creditCardService.findCardsByClientId(clientId)
                .map(CreditCardMapper::toCreditCardDetails);
        return Mono.just(ResponseEntity.ok(creditCardDetailsFlux));
    }
}
