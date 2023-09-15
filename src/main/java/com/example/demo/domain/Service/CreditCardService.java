package com.example.demo.domain.Service;
import com.example.demo.application.ICreditCardService;
import com.example.demo.domain.document.CreditCard;
import com.example.demo.domain.Repository.CreditCardRepository;
import com.example.demo.infraestructure.WebClient.TransactionRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.api.DTOS.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CreditCardService implements ICreditCardService {
    /**
     * Coneccion de la BD CreditCardRepository.
     */
    @Autowired
    private CreditCardRepository creditCardRepository;
    /**
     * injeccion de TransactionRestClient.
     */
    @Autowired
    private TransactionRestClient transactionRestClient;

    /**
     * injeccion de SecureRandom random.
     */
    private static final SecureRandom random = new SecureRandom();
    private final int MAX_ATTEMPTS = 10;
    /**
     * CreditCardService.
     * @param cardNumber variable.
     * @return CreditCard.
     */
    @Override
    public Mono<CreditCard> findCardByNumber(final String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber);
    }

    /**
     * CreditCardService.
     * @param creditCard de CreditCard.
     * @return CreditCard.
     */
    @Override
    public Mono<CreditCard> saveCreditCard(final CreditCard creditCard) {
        if (creditCard.getCardNumber() == null || creditCard.getCardNumber().isEmpty()) {
            return generateUniqueCardNumber(MAX_ATTEMPTS)
                    .doOnNext(creditCard::setCardNumber)
                    .flatMap(num -> creditCardRepository.save(creditCard));
        }
        return creditCardRepository.save(creditCard);
    }

    /**
     * CreditCardService.
     * @param cardNumber variable.
     * @param amount variable.
     */
    @Override
    public Mono<Void> makePayment(final String cardNumber, final Double amount) {
        return findCardByNumber(cardNumber)
                .doOnNext(creditCard -> {
                    creditCard.setBalance(creditCard.getBalance() - amount);
                    creditCard.setAvailableCredit(creditCard.getLimit() - creditCard.getBalance());
                })
                .flatMap(this::saveCreditCard)
                .flatMap(creditCard -> sendTransactionToService(creditCard.getClientId(), "CREDIT_CARD", "Payment", amount));
    }

    /**
     * CreditCardService.
     * @param cardNumber variable.
     * @param amount variable.
     */
    @Override
    public Mono<Void> registerPurchase(final String cardNumber, final Double amount, final  String description) {
        return findCardByNumber(cardNumber)
                .filter(creditCard -> creditCard.getAvailableCredit() >= amount)
                .doOnNext(creditCard -> {
                    creditCard.setBalance(creditCard.getBalance() + amount);
                    creditCard.setAvailableCredit(creditCard.getLimit() - creditCard.getBalance());
                })
                .flatMap(this::saveCreditCard)
                .flatMap(creditCard -> sendTransactionToService(creditCard.getClientId(), "CREDIT_CARD", description, amount));
    }

    private Mono<String> generateUniqueCardNumber(int remainingAttempts) {
        if (remainingAttempts <= 0) {
            return Mono.error(new RuntimeException("Max attempts reached for generating unique card number"));
        }
        return Mono.fromSupplier(this::generateRandomCardNumber)
                .filterWhen(this::isCardNumberNotExist)
                .switchIfEmpty(generateUniqueCardNumber(remainingAttempts - 1));
    }

    private String generateRandomCardNumber() {
        return IntStream.range(0, 16)
                .mapToObj(i -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining());
    }

    /**
     * CreditCardService.
     * @param cardNumber variable.
     * @return creditCardRepository.
     */

    private Mono<Boolean> isCardNumberNotExist(final String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber)
                .hasElement()
                .map(exists -> !exists);
    }

    /**
     * CreditCardService.
     * @param clientId variable.
     * @param type variable.
     * @param description variable.
     * @param amount variable.
     */
    private Mono<Void> sendTransactionToService(final String clientId, final String type, final String description, final Double amount) {
        Transaction transaction = new Transaction();
        transaction.setClientId(clientId);
        transaction.setIdTransactionType(type);
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setTransactionDate(OffsetDateTime.now());  // Cambiado a la fecha/hora actual
        return transactionRestClient.sendTransaction(transaction);
    }

    /**
     * CreditCardService.
     * @param clientId variable.
     * @return CreditCard.
     */
    @Override
    public Flux<CreditCard> findCardsByClientId(final String clientId) {
        return creditCardRepository.findByClientId(clientId);
    }
}


