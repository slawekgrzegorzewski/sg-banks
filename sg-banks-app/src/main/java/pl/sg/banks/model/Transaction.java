package pl.sg.banks.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "nodrigen_transaction")
public class Transaction {
    @Id

    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    @Enumerated(EnumType.STRING)
    private TransactionPhase phase;
    private String additionalInformation;
    private String additionalInformationStructured;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "balanceAmount.amount", column = @Column(name = "balance_after_transaction_amount")),
            @AttributeOverride(name = "balanceAmount.currency", column = @Column(name = "balance_after_transaction_currency")),
            @AttributeOverride(name = "balanceType", column = @Column(name = "balance_after_transaction_balance_type")),
            @AttributeOverride(name = "creditLimitIncluded", column = @Column(name = "balance_after_transaction_credit_limit_included")),
            @AttributeOverride(name = "lastChangeDateTime", column = @Column(name = "balance_after_transaction_last_change_date_time")),
            @AttributeOverride(name = "lastCommittedTransaction", column = @Column(name = "balance_after_transaction_last_committed_transaction")),
            @AttributeOverride(name = "referenceDate", column = @Column(name = "balance_after_transaction_reference_date"))
    })
    private BalanceEmbeddable balanceAfterTransaction;
    private String bankTransactionCode;
    private LocalDate bookingDate;
    private OffsetDateTime bookingDateTime;
    private String checkId;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bban", column = @Column(name = "creditor_account_bban")),
            @AttributeOverride(name = "iban", column = @Column(name = "creditor_account_iban"))
    })
    private AccountEmbeddable creditorAccount;
    private String creditorAgent;
    private String creditorId;
    private String creditorName;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "exchangeRate", column = @Column(name = "currency_exchange_rate")),
            @AttributeOverride(name = "sourceCurrency", column = @Column(name = "currency_exchange_source_currency")),
            @AttributeOverride(name = "targetCurrency", column = @Column(name = "currency_exchange_target_currency")),
            @AttributeOverride(name = "unitCurrency", column = @Column(name = "currency_exchange_unit_currency")),
            @AttributeOverride(name = "instructedAmount.currency", column = @Column(name = "currency_exchange_instructed_amount_currency")),
            @AttributeOverride(name = "instructedAmount.amount", column = @Column(name = "currency_exchange_instructed_amount_amount"))
    })
    private CurrencyExchangeEmbeddable currencyExchange;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "bban", column = @Column(name = "debtor_account_bban")),
            @AttributeOverride(name = "iban", column = @Column(name = "debtor_account_iban"))
    })
    private AccountEmbeddable debtorAccount;
    private String debtorAgent;
    private String debtorName;
    private String entryReference;
    private String mandateId;
    private String purposeCode;
    private String proprietaryBankTransactionCode;
    private String remittanceInformationStructured;
    private String remittanceInformationStructuredArray;
    private String remittanceInformationUnstructured;
    private String remittanceInformationUnstructuredArray;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "transaction_amount_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "transaction_amount_currency"))
    })
    private AmountEmbeddable transactionAmount;
    private String transactionId;
    private String ultimateCreditor;
    private String ultimateDebtor;
    private LocalDate valueDate;
    private OffsetDateTime valueDateTime;
    private LocalDateTime importTime;
    @ManyToOne
    private BankAccount bankAccount;

    public Integer getId() {
        return id;
    }

    public Transaction setId(Integer id) {
        this.id = id;
        return this;
    }

    public TransactionPhase getPhase() {
        return phase;
    }

    public Transaction setPhase(TransactionPhase phase) {
        this.phase = phase;
        return this;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public Transaction setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
        return this;
    }

    public String getAdditionalInformationStructured() {
        return additionalInformationStructured;
    }

    public Transaction setAdditionalInformationStructured(String additionalInformationStructured) {
        this.additionalInformationStructured = additionalInformationStructured;
        return this;
    }

    public BalanceEmbeddable getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public Transaction setBalanceAfterTransaction(BalanceEmbeddable balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
        return this;
    }

    public String getBankTransactionCode() {
        return bankTransactionCode;
    }

    public Transaction setBankTransactionCode(String bankTransactionCode) {
        this.bankTransactionCode = bankTransactionCode;
        return this;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public Transaction setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
        return this;
    }

    public OffsetDateTime getBookingDateTime() {
        return bookingDateTime;
    }

    public Transaction setBookingDateTime(OffsetDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
        return this;
    }

    public String getCheckId() {
        return checkId;
    }

    public Transaction setCheckId(String checkId) {
        this.checkId = checkId;
        return this;
    }

    public AccountEmbeddable getCreditorAccount() {
        return creditorAccount;
    }

    public Transaction setCreditorAccount(AccountEmbeddable creditorAccount) {
        this.creditorAccount = creditorAccount;
        return this;
    }

    public String getCreditorAgent() {
        return creditorAgent;
    }

    public Transaction setCreditorAgent(String creditorAgent) {
        this.creditorAgent = creditorAgent;
        return this;
    }

    public String getCreditorId() {
        return creditorId;
    }

    public Transaction setCreditorId(String creditorId) {
        this.creditorId = creditorId;
        return this;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public Transaction setCreditorName(String creditorName) {
        this.creditorName = creditorName;
        return this;
    }

    public CurrencyExchangeEmbeddable getCurrencyExchange() {
        return currencyExchange;
    }

    public Transaction setCurrencyExchange(CurrencyExchangeEmbeddable currencyExchange) {
        this.currencyExchange = currencyExchange;
        return this;
    }

    public AccountEmbeddable getDebtorAccount() {
        return debtorAccount;
    }

    public Transaction setDebtorAccount(AccountEmbeddable debtorAccount) {
        this.debtorAccount = debtorAccount;
        return this;
    }

    public String getDebtorAgent() {
        return debtorAgent;
    }

    public Transaction setDebtorAgent(String debtorAgent) {
        this.debtorAgent = debtorAgent;
        return this;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public Transaction setDebtorName(String debtorName) {
        this.debtorName = debtorName;
        return this;
    }

    public String getEntryReference() {
        return entryReference;
    }

    public Transaction setEntryReference(String entryReference) {
        this.entryReference = entryReference;
        return this;
    }

    public String getMandateId() {
        return mandateId;
    }

    public Transaction setMandateId(String mandateId) {
        this.mandateId = mandateId;
        return this;
    }

    public String getPurposeCode() {
        return purposeCode;
    }

    public Transaction setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
        return this;
    }

    public String getProprietaryBankTransactionCode() {
        return proprietaryBankTransactionCode;
    }

    public Transaction setProprietaryBankTransactionCode(String proprietaryBankTransactionCode) {
        this.proprietaryBankTransactionCode = proprietaryBankTransactionCode;
        return this;
    }

    public String getRemittanceInformationStructured() {
        return remittanceInformationStructured;
    }

    public Transaction setRemittanceInformationStructured(String remittanceInformationStructured) {
        this.remittanceInformationStructured = remittanceInformationStructured;
        return this;
    }

    public String getRemittanceInformationStructuredArray() {
        return remittanceInformationStructuredArray;
    }

    public Transaction setRemittanceInformationStructuredArray(String remittanceInformationStructuredArray) {
        this.remittanceInformationStructuredArray = remittanceInformationStructuredArray;
        return this;
    }

    public String getRemittanceInformationUnstructured() {
        return remittanceInformationUnstructured;
    }

    public Transaction setRemittanceInformationUnstructured(String remittanceInformationUnstructured) {
        this.remittanceInformationUnstructured = remittanceInformationUnstructured;
        return this;
    }

    public String getRemittanceInformationUnstructuredArray() {
        return remittanceInformationUnstructuredArray;
    }

    public Transaction setRemittanceInformationUnstructuredArray(String remittanceInformationUnstructuredArray) {
        this.remittanceInformationUnstructuredArray = remittanceInformationUnstructuredArray;
        return this;
    }

    public AmountEmbeddable getTransactionAmount() {
        return transactionAmount;
    }

    public Transaction setTransactionAmount(AmountEmbeddable transactionAmount) {
        this.transactionAmount = transactionAmount;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Transaction setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getUltimateCreditor() {
        return ultimateCreditor;
    }

    public Transaction setUltimateCreditor(String ultimateCreditor) {
        this.ultimateCreditor = ultimateCreditor;
        return this;
    }

    public String getUltimateDebtor() {
        return ultimateDebtor;
    }

    public Transaction setUltimateDebtor(String ultimateDebtor) {
        this.ultimateDebtor = ultimateDebtor;
        return this;
    }

    public LocalDate getValueDate() {
        return valueDate;
    }

    public Transaction setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
        return this;
    }

    public OffsetDateTime getValueDateTime() {
        return valueDateTime;
    }

    public Transaction setValueDateTime(OffsetDateTime valueDateTime) {
        this.valueDateTime = valueDateTime;
        return this;
    }

    public LocalDateTime getImportTime() {
        return importTime;
    }

    public Transaction setImportTime(LocalDateTime importTime) {
        this.importTime = importTime;
        return this;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public Transaction setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

}
