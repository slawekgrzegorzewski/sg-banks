package pl.sg.banks.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class AccountEmbeddable {
    public String bban;
    public String iban;

    public String getBban() {
        return bban;
    }

    public AccountEmbeddable setBban(String bban) {
        this.bban = bban;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public AccountEmbeddable setIban(String iban) {
        this.iban = iban;
        return this;
    }
}
