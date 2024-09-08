package pl.sg.go_cardless.model;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
public class Amount {
    public BigDecimal amount;
    public Currency currency;

    public BigDecimal getAmount() {
        return amount;
    }

    public Amount setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Amount setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }
}
