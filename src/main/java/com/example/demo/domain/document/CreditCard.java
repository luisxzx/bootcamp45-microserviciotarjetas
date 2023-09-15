package com.example.demo.domain.document;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * Clase que representa una entidad de CreditCard.
 */
@Data
@Builder
@Document(collection = "creditCards")
public class CreditCard {
    /**
     * Identificador único de la AccountDocument.
     */
    @Id
    private String id;

    /**
     * Identificador único de la clientId.
     */
    private String clientId;

    /**
     * Identificador único de la limit.
     */
    private Double limit;

    /**
     * Identificador único de la cardNumber.
     */
    private String cardNumber;

    /**
     * Identificador único de la balance.
     */
    private Double balance;

    /**
     * Identificador único de la availableCredit.
     */
    private Double availableCredit;
}
