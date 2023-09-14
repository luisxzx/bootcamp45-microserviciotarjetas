package com.example.demo.common.mapper;
import com.example.demo.domain.document.CreditCard;
import com.example.demo.model.CreditCardBalance;
import com.example.demo.model.CreditCardCreateInput;
import com.example.demo.model.CreditCardDetails;
import java.math.BigDecimal;
public class CreditCardMapper {
    /**
     * Método para CreditCardMapper.
     * @param creditCard parametro de CreditCard.
     * @return CreditCardDetails.
     */
    public static CreditCardDetails toCreditCardDetails(final CreditCard creditCard) {
        CreditCardDetails creditCardDetails = new CreditCardDetails();
        creditCardDetails.setId(creditCard.getId());
        creditCardDetails.setClientId(creditCard.getClientId());
        creditCardDetails.setAvailableCredit(new BigDecimal(creditCard.getAvailableCredit()));
        creditCardDetails.setBalance(new BigDecimal(creditCard.getBalance()));
        creditCardDetails.setLimit(new BigDecimal(creditCard.getLimit()));
        creditCardDetails.setCardNumber(creditCard.getCardNumber());
        return creditCardDetails;
    }

    /**
     * Método para CreditCardMapper.
     * @param creditCardCreateInput parametro de CreditCardCreateInput.
     * @return CreditCard.
     */
    public static CreditCard fromCreditCardCreateInput(final CreditCardCreateInput creditCardCreateInput) {
        return CreditCard.builder()
                .clientId(creditCardCreateInput.getClientId())
                .limit(creditCardCreateInput.getLimit().doubleValue()) // Convertir BigDecimal a Double
                .cardNumber(creditCardCreateInput.getCardNumber())
                .balance(0.0) // balance inicial es 0
                .availableCredit(creditCardCreateInput.getLimit().doubleValue()) // Convertir BigDecimal a Double
                .build();
    }

    /**
     * Método para CreditCardMapper.
     * @param creditCard parametro de CreditCard.
     * @return CreditCardBalance.
     */
    public static CreditCardBalance toCreditCardBalance(final CreditCard creditCard) {
        CreditCardBalance creditCardBalance = new CreditCardBalance();
        creditCardBalance.setBalance(new BigDecimal(Double.toString(creditCard.getBalance())));
        creditCardBalance.setAvailableCredit(new BigDecimal(Double.toString(creditCard.getAvailableCredit())));
        return creditCardBalance;
    }
}
