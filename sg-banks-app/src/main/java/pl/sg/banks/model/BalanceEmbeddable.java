package pl.sg.banks.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Embeddable
public class BalanceEmbeddable {
    @Embedded
    private AmountEmbeddable balanceAmount;
    private String balanceType;
    public Boolean creditLimitIncluded;
    public OffsetDateTime lastChangeDateTime;
    public String lastCommittedTransaction;
    private LocalDate referenceDate;

    public AmountEmbeddable getBalanceAmount() {
        return balanceAmount;
    }

    public BalanceEmbeddable setBalanceAmount(AmountEmbeddable balanceAmount) {
        this.balanceAmount = balanceAmount;
        return this;
    }

    public String getBalanceType() {
        return balanceType;
    }

    public BalanceEmbeddable setBalanceType(String balanceType) {
        this.balanceType = balanceType;
        return this;
    }

    public Boolean getCreditLimitIncluded() {
        return creditLimitIncluded;
    }

    public BalanceEmbeddable setCreditLimitIncluded(Boolean creditLimitIncluded) {
        this.creditLimitIncluded = creditLimitIncluded;
        return this;
    }

    public OffsetDateTime getLastChangeDateTime() {
        return lastChangeDateTime;
    }

    public BalanceEmbeddable setLastChangeDateTime(OffsetDateTime lastChangeDateTime) {
        this.lastChangeDateTime = lastChangeDateTime;
        return this;
    }

    public String getLastCommittedTransaction() {
        return lastCommittedTransaction;
    }

    public BalanceEmbeddable setLastCommittedTransaction(String lastCommittedTransaction) {
        this.lastCommittedTransaction = lastCommittedTransaction;
        return this;
    }

    public LocalDate getReferenceDate() {
        return referenceDate;
    }

    public BalanceEmbeddable setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
        return this;
    }
}
