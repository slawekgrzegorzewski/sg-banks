package pl.sg.go_cardless.rest.transactions;


import pl.sg.go_cardless.rest.balances.Amount;

import java.math.BigDecimal;
import java.util.Currency;

public class CurrencyExchange {
    public BigDecimal exchangeRate;
    public Amount instructedAmount;
    public Currency sourceCurrency;
    public Currency targetCurrency;
    public Currency unitCurrency;
}
