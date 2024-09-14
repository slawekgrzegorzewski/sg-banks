package pl.sg.banks.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
public class AmountEmbeddable {
    public BigDecimal amount;
    public Currency currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public AmountEmbeddable setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AmountEmbeddable setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }
}
