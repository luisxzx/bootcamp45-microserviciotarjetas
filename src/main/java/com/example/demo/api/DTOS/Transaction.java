package com.example.demo.api.DTOS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Clase que representa una entidad de Transaction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    /**
     * Identificador único de la id.
     */
    @Id
    private String id;

    /**
     * Identificador único de la clientId.
     */
    private String clientId;

    /**
     * Identificador único de la sourceType.
     */

    private String idProductTypeSource;

    /**
     * Identificador único de la sourceNumber.
     */
    private String sourceNumber;

    /**
     * Identificador único de la destinyType.
     */
    private String idProductTypeDestiny;

    /**
     * Identificador único de la destinyNumber.
     */
    private String destinyNumber;

    /**
     * Identificador único de la amount.
     */
    private Double amount;

    /**
     * Identificador único de la description.
     */
    private String description;

    /**
     * Identificador único de la transactionDate.
     */
    private OffsetDateTime transactionDate;

    /**
     * Identificador único de la transactionType.
     */

    private String idTransactionType;
}

