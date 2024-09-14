package pl.sg.banks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Currency;
import java.util.List;

@Entity
public class BankAccount {
    @Id

    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    @NotNull
    private String iban;
    @NotNull
    private Currency currency;
    @NotNull
    private String owner;
    private String product;
    private String bic;
    @NotNull
    private String externalId;
    @ManyToOne
    private BankPermission bankPermission;
    @OneToMany(mappedBy = "bankAccount")
    private List<Transaction> nodrigenTransactions;
    @OneToMany(mappedBy = "bankAccount")
    private List<BankAccountBalance> nodrigenBalances;

    public Integer getId() {
        return id;
    }

    public BankAccount setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getIban() {
        return iban;
    }

    public BankAccount setIban(String iban) {
        this.iban = iban;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BankAccount setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public BankAccount setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getProduct() {
        return product;
    }

    public BankAccount setProduct(String product) {
        this.product = product;
        return this;
    }

    public String getBic() {
        return bic;
    }

    public BankAccount setBic(String bic) {
        this.bic = bic;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public BankAccount setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public BankPermission getBankPermission() {
        return bankPermission;
    }

    public BankAccount setBankPermission(BankPermission bankPermission) {
        this.bankPermission = bankPermission;
        return this;
    }

    public List<Transaction> getNodrigenTransactions() {
        return nodrigenTransactions;
    }

    public BankAccount setNodrigenTransactions(List<Transaction> nodrigenTransactions) {
        this.nodrigenTransactions = nodrigenTransactions;
        return this;
    }

    public List<BankAccountBalance> getNodrigenBalances() {
        return nodrigenBalances;
    }

    public BankAccount setNodrigenBalances(List<BankAccountBalance> nodrigenBalances) {
        this.nodrigenBalances = nodrigenBalances;
        return this;
    }
}
