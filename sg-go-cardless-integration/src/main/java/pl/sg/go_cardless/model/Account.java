package pl.sg.go_cardless.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Account {
    public String bban;
    public String iban;

    public String getBban() {
        return bban;
    }

    public Account setBban(String bban) {
        this.bban = bban;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public Account setIban(String iban) {
        this.iban = iban;
        return this;
    }
}
