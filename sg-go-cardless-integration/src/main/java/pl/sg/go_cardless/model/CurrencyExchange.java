package pl.sg.go_cardless.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.math.BigDecimal;
import java.util.Currency;

@Embeddable
public class CurrencyExchange {
    public BigDecimal exchangeRate;
    @Embedded
    public Amount instructedAmount;
    public Currency sourceCurrency;
    public Currency targetCurrency;
    public Currency unitCurrency;

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public CurrencyExchange setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
        return this;
    }

    public Amount getInstructedAmount() {
        return instructedAmount;
    }

    public CurrencyExchange setInstructedAmount(Amount instructedAmount) {
        this.instructedAmount = instructedAmount;
        return this;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public CurrencyExchange setSourceCurrency(Currency sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public CurrencyExchange setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
        return this;
    }

    public Currency getUnitCurrency() {
        return unitCurrency;
    }

    public CurrencyExchange setUnitCurrency(Currency unitCurrency) {
        this.unitCurrency = unitCurrency;
        return this;
    }
}
