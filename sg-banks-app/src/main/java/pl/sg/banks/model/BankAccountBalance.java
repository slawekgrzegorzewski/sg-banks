package pl.sg.banks.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "nodrigen_bank_account_balance")
public class BankAccountBalance {
    @Id

    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    private LocalDateTime fetchTime;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_amount_currency"))
    })
    private AmountEmbeddable balanceAmount;
    private String balanceType;
    private Boolean creditLimitIncluded;
    private OffsetDateTime lastChangeDateTime;
    private String lastCommittedTransaction;
    private LocalDate referenceDate;
    @ManyToOne
    private BankAccount bankAccount;

    public Integer getId() {
        return id;
    }

    public BankAccountBalance setId(Integer id) {
        this.id = id;
        return this;
    }

    public AmountEmbeddable getBalanceAmount() {
        return balanceAmount;
    }

    public BankAccountBalance setBalanceAmount(AmountEmbeddable balanceAmount) {
        this.balanceAmount = balanceAmount;
        return this;
    }

    public LocalDateTime getFetchTime() {
        return fetchTime;
    }

    public BankAccountBalance setFetchTime(LocalDateTime fetchTime) {
        this.fetchTime = fetchTime;
        return this;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public BankAccountBalance setBalanceType(String balanceType) {
        this.balanceType = balanceType;
        return this;
    }

    public Boolean getCreditLimitIncluded() {
        return creditLimitIncluded;
    }

    public BankAccountBalance setCreditLimitIncluded(Boolean creditLimitIncluded) {
        this.creditLimitIncluded = creditLimitIncluded;
        return this;
    }

    public OffsetDateTime getLastChangeDateTime() {
        return lastChangeDateTime;
    }

    public BankAccountBalance setLastChangeDateTime(OffsetDateTime lastChangeDateTime) {
        this.lastChangeDateTime = lastChangeDateTime;
        return this;
    }

    public String getLastCommittedTransaction() {
        return lastCommittedTransaction;
    }

    public BankAccountBalance setLastCommittedTransaction(String lastCommittedTransaction) {
        this.lastCommittedTransaction = lastCommittedTransaction;
        return this;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public BankAccountBalance setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
        return this;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public BankAccountBalance setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }
}
