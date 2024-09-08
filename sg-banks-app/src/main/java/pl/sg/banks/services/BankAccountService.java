package pl.sg.banks.services;

import pl.sg.banks.model.BankAccount;

public interface BankAccountService {

    void fetchAllTransactions();

    void fetchAccountTransactions();

    void fetch(String bankAccountExternalId);

    void fetchAllBalances();

    void fetchAccountBalances(BankAccount bankAccount);
}
