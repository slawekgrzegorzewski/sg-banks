package pl.sg.go_cardless.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountDetails {
    private final Account account;

    public AccountDetails(@JsonProperty("account") Account account) {
        this.account = account;
    }

    @JsonProperty("account")
    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "account=" + account +
                '}';
    }
}
