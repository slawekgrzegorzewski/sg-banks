package pl.sg.banks.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class BankPermission {
    @Id

    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    protected Integer id;
    protected LocalDateTime createdAt;
    protected LocalDateTime givenAt;
    protected LocalDateTime withdrawnAt;
    @OneToMany(mappedBy = "bankPermission")
    protected List<BankAccount> bankAccounts;

    public Integer getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getGivenAt() {
        return givenAt;
    }

    public void setGivenAt(LocalDateTime givenAt) {
        this.givenAt = givenAt;
    }

    public LocalDateTime getWithdrawnAt() {
        return withdrawnAt;
    }

    public void setWithdrawnAt(LocalDateTime withdrawnAt) {
        this.withdrawnAt = withdrawnAt;
    }

    public BankPermission setId(Integer id) {
        this.id = id;
        return this;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public BankPermission setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
        return this;
    }
}
