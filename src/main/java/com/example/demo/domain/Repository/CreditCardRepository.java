package com.example.demo.domain.Repository;
import com.example.demo.domain.document.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interfaz que define un AccountMongoRepository para AccountDocument.
 * */
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
    /**
     * AccountMongoRepository.
     * @param cardNumber variable.
     * @return CreditCard.
     */
    Mono<CreditCard> findByCardNumber(String cardNumber);

    /**
     * AccountMongoRepository.
     * @param clientId variable.
     * @return CreditCard.
     */
    Flux<CreditCard> findByClientId(String clientId);
}
