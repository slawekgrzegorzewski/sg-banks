package pl.sg.banks.model;

import jakarta.persistence.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private String institutionId;
    @Column(columnDefinition = "TEXT")
    private String reference;
    @Column(columnDefinition = "TEXT")
    private String ssn;
    private URL confirmationLink;
    private UUID requisitionId;

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

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public URL getConfirmationLink() {
        return confirmationLink;
    }

    public void setConfirmationLink(URL confirmationLink) {
        this.confirmationLink = confirmationLink;
    }

    public UUID getRequisitionId() {
        return requisitionId;
    }

    public BankPermission setRequisitionId(UUID requisitionId) {
        this.requisitionId = requisitionId;
        return this;
    }
}
