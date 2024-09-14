package pl.sg.banks.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
public class CurrencyExchangeEmbeddable {
    public BigDecimal exchangeRate;
    @Embedded
    public AmountEmbeddable instructedAmount;
    public Currency sourceCurrency;
    public Currency targetCurrency;
    public Currency unitCurrency;

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public CurrencyExchangeEmbeddable setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public AmountEmbeddable getInstructedAmount() {
        return instructedAmount;
    }

    public CurrencyExchangeEmbeddable setInstructedAmount(AmountEmbeddable instructedAmount) {
        this.instructedAmount = instructedAmount;
        return this;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public CurrencyExchangeEmbeddable setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public CurrencyExchangeEmbeddable setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }

    public Currency getUnitCurrency() {
        return unitCurrency;
    }

    public CurrencyExchangeEmbeddable setUnitCurrency(Currency unitCurrency) {
        this.unitCurrency = unitCurrency;
        return this;
    }
}
