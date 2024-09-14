package pl.sg.banks.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import pl.sg.banks.model.*;
import pl.sg.banks.repositories.BankAccountBalanceRepository;
import pl.sg.banks.repositories.BankAccountRepository;
import pl.sg.banks.repositories.BankPermissionRepository;
import pl.sg.banks.repositories.TransactionRepository;
import pl.sg.go_cardless.rest.balances.Amount;
import pl.sg.go_cardless.rest.balances.Balance;
import pl.sg.go_cardless.rest.transactions.Account;
import pl.sg.go_cardless.rest.transactions.CurrencyExchange;
import pl.sg.go_cardless.rest.transactions.Transactions;
import pl.sg.go_cardless.service.EUAEndedException;
import pl.sg.go_cardless.service.GoCardlessClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Controller
public class BankAccountServiceImpl implements BankAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountRepository bankAccountRepository;
    private final GoCardlessClient goCardlessClient;
    private final BankAccountBalanceRepository bankAccountBalanceRepository;
    private final TransactionRepository transactionRepository;
    private final BankPermissionRepository bankPermissionRepository;

    public BankAccountServiceImpl(
            BankAccountRepository bankAccountRepository,
            GoCardlessClient goCardlessClient,
            BankAccountBalanceRepository bankAccountBalanceRepository,
            TransactionRepository transactionRepository,
            BankPermissionRepository bankPermissionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.goCardlessClient = goCardlessClient;
        this.bankAccountBalanceRepository = bankAccountBalanceRepository;
        this.transactionRepository = transactionRepository;
        this.bankPermissionRepository = bankPermissionRepository;
    }

    @Override
    public void fetchAllTransactions() {
        bankAccountRepository.findBankAccountsToFetchData().forEach(this::fetchAccountTransactions);
    }

    @Override
    public void fetchAccountTransactions(BankAccount bankAccount) {
        try {
            Transactions allTransactionsFromNodrigen =
                    goCardlessClient.getTransactions(UUID.fromString(bankAccount.getExternalId()))
                            .map(t -> t.transactions)
                            .orElseGet(Transactions::new);
            List<String> allNodrigenTransactionIds = Stream.concat(
                            allTransactionsFromNodrigen.booked.stream(),
                            allTransactionsFromNodrigen.pending.stream()
                    )
                    .map(transaction -> transaction.transactionId)
                    .collect(Collectors.toList());

            Map<String, List<Transaction>> existingTransactions = transactionRepository.findAllByTransactionIdIn(allNodrigenTransactionIds)
                    .stream()
                    .collect(Collectors.groupingBy(
                            Transaction::getTransactionId,
                            Collectors.toList()
                    ));

            LocalDateTime now = LocalDateTime.now();
            List<Transaction> toSave = new ArrayList<>();
            allTransactionsFromNodrigen.booked.stream()
                    .filter(transaction -> transactionNotPresentInDb(existingTransactions, transaction, bankAccount))
                    .map(transaction -> mapToDb(transaction, bankAccount, now, TransactionPhase.BOOKED))
                    .collect(Collectors.toCollection(() -> toSave));
            allTransactionsFromNodrigen.pending.stream()
                    .filter(transaction -> transactionNotPresentInDb(existingTransactions, transaction, bankAccount))
                    .map(transaction -> mapToDb(transaction, bankAccount, now, TransactionPhase.PENDING))
                    .collect(Collectors.toCollection(() -> toSave));
            transactionRepository.saveAll(toSave);
        } catch (EUAEndedException exception) {
            LOG.warn("Withdrawing permission for bank account %d and external id %s due to".formatted(bankAccount.getId(), bankAccount.getExternalId()), exception);
            recreateRequisition(bankAccount);
        }
    }

    private void recreateRequisition(BankAccount bankAccount) {
        bankPermissionRepository.getPermissionsGrantedForBankAccount(bankAccount)
                .ifPresent((permission) -> {
                    permission.setWithdrawnAt(LocalDateTime.now());
                    bankPermissionRepository.save(permission);
                });
    }

    private boolean transactionNotPresentInDb(Map<String, List<Transaction>> existingTransactions, pl.sg.go_cardless.rest.transactions.Transaction transaction, BankAccount bankAccount) {
        return existingTransactions.getOrDefault(transaction.transactionId, List.of())
                .stream()
                .noneMatch(nodrigenTransaction -> nodrigenTransaction.getBankAccount().getId().equals(bankAccount.getId()));
    }

    private Transaction mapToDb(pl.sg.go_cardless.rest.transactions.Transaction transaction, BankAccount bankAccount, LocalDateTime now, TransactionPhase phase) {
        return new Transaction()
                .setBankAccount(bankAccount)
                .setImportTime(now)
                .setPhase(phase)
                .setAdditionalInformation(transaction.additionalInformation)
                .setAdditionalInformationStructured(transaction.additionalInformationStructured)
                .setBookingDate(transaction.bookingDate)
                .setBalanceAfterTransaction(mapNodrigenBalanceAfterTransaction(transaction.balanceAfterTransaction))
                .setBankTransactionCode(transaction.bankTransactionCode)
                .setBookingDateTime(transaction.bookingDateTime)
                .setCheckId(transaction.checkId)
                .setCreditorAccount(mapNodrigenAccount(transaction.creditorAccount))
                .setCreditorAgent(transaction.creditorAgent)
                .setCreditorId(transaction.creditorId)
                .setCreditorName(transaction.creditorName)
                .setCurrencyExchange(mapNodrigenCurrencyExchange(transaction.currencyExchange))
                .setDebtorAccount(mapNodrigenAccount(transaction.debtorAccount))
                .setDebtorAgent(transaction.debtorAgent)
                .setDebtorName(transaction.debtorName)
                .setEntryReference(transaction.entryReference)
                .setMandateId(transaction.mandateId)
                .setProprietaryBankTransactionCode(transaction.proprietaryBankTransactionCode)
                .setPurposeCode(transaction.purposeCode)
                .setRemittanceInformationStructured(transaction.remittanceInformationStructured)
                .setRemittanceInformationStructuredArray(ofNullable(transaction.remittanceInformationStructuredArray).map(Arrays::toString).orElse(null))
                .setRemittanceInformationUnstructured(transaction.remittanceInformationUnstructured)
                .setRemittanceInformationUnstructuredArray(ofNullable(transaction.remittanceInformationUnstructuredArray).map(Arrays::toString).orElse(null))
                .setTransactionAmount(mapNodrigenAmount(transaction.transactionAmount))
                .setTransactionId(transaction.transactionId)
                .setUltimateCreditor(transaction.ultimateCreditor)
                .setUltimateDebtor(transaction.ultimateDebtor)
                .setValueDate(transaction.valueDate)
                .setValueDateTime(transaction.valueDateTime);
    }

    private BalanceEmbeddable mapNodrigenBalanceAfterTransaction(Balance balance) {
        return ofNullable(balance)
                .map(bat -> new BalanceEmbeddable()
                        .setBalanceAmount(mapNodrigenBalanceAmount(bat.balanceAmount))
                        .setBalanceType(bat.balanceType)
                        .setCreditLimitIncluded(bat.creditLimitIncluded)
                        .setLastCommittedTransaction(bat.lastCommittedTransaction)
                        .setLastChangeDateTime(bat.lastChangeDateTime)
                        .setReferenceDate(bat.referenceDate))
                .orElse(null);
    }

    private AmountEmbeddable mapNodrigenBalanceAmount(Amount amount) {
        return ofNullable(amount)
                .map(ba -> new AmountEmbeddable().setAmount(ba.amount).setCurrency(ba.currency))
                .orElse(null);
    }

    private CurrencyExchangeEmbeddable mapNodrigenCurrencyExchange(CurrencyExchange currencyExchange) {
        return ofNullable(currencyExchange)
                .map(ce -> new CurrencyExchangeEmbeddable()
                        .setExchangeRate(ce.exchangeRate)
                        .setInstructedAmount(mapNodrigenAmount(ce.instructedAmount))
                        .setSourceCurrency(ce.sourceCurrency)
                        .setTargetCurrency(ce.targetCurrency)
                        .setUnitCurrency(ce.unitCurrency))
                .orElse(null);
    }

    private AccountEmbeddable mapNodrigenAccount(Account creditorAccount) {
        return ofNullable(creditorAccount)
                .map(ca -> new AccountEmbeddable().setBban(ca.bban).setIban(ca.iban))
                .orElse(null);
    }

    private AmountEmbeddable mapNodrigenAmount(Amount transactionAmount) {
        return ofNullable(transactionAmount)
                .map(ta -> new AmountEmbeddable().setAmount(ta.amount).setCurrency(ta.currency))
                .orElse(null);
    }

    @Override
    public void fetch(String bankAccountExternalId) {
        BankAccount bankAccount = bankAccountRepository.getBankAccountByExternalId(bankAccountExternalId);
        fetchAccountBalances(bankAccount);
        fetchAccountTransactions(bankAccount);
    }

    @Override
    public void fetchAllBalances() {
        bankAccountRepository.findBankAccountsToFetchData().forEach(this::fetchAccountBalances);
    }

    @Override
    public void fetchAccountBalances(BankAccount bankAccount) {
        try {
            Balance[] allTransactionsFromNodrigen = goCardlessClient
                    .getBalances(UUID.fromString(bankAccount.getExternalId()))
                    .map(b -> b.balances)
                    .orElseGet(() -> new Balance[0]);
            LocalDateTime now = LocalDateTime.now();
            List<BankAccountBalance> toSave = new ArrayList<>();
            Arrays.stream(allTransactionsFromNodrigen)
                    .map(balance -> mapBalance(balance, bankAccount, now))
                    .collect(Collectors.toCollection(() -> toSave));
            bankAccountBalanceRepository.saveAll(toSave);
        } catch (EUAEndedException exception) {
            LOG.warn("Withdrawing permission for bank account %d and external id %s due to".formatted(bankAccount.getId(), bankAccount.getExternalId()), exception);
            recreateRequisition(bankAccount);
        }

    }

    private BankAccountBalance mapBalance(Balance balance, BankAccount bankAccount, LocalDateTime now) {
        return new BankAccountBalance()
                .setBankAccount(bankAccount)
                .setFetchTime(now)
                .setBalanceAmount(mapNodrigenBalanceAmount(balance.balanceAmount))
                .setBalanceType(balance.balanceType)
                .setCreditLimitIncluded(balance.creditLimitIncluded)
                .setLastChangeDateTime(balance.lastChangeDateTime)
                .setLastCommittedTransaction(balance.lastCommittedTransaction)
                .setReferenceDate(balance.referenceDate);
    }
}
