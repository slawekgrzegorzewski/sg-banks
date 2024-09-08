package pl.sg.banks.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class BankAccountsDataFetcher {

    private final BankAccountService bankAccountService;

    public BankAccountsDataFetcher(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Scheduled(cron = "${cron.fetch-transactions}", zone = "Europe/Warsaw")
    public void fetchAllTransactions() {
        bankAccountService.fetchAllTransactions();
    }

    @Scheduled(cron = "${cron.fetch-accounts}", zone = "Europe/Warsaw")
    public void fetchAllBalances() {
        bankAccountService.fetchAllBalances();
    }

    @Scheduled(cron = "${cron.fetch-accounts-monthly}", zone = "Europe/Warsaw")
    public void fetchAllBalancesAtTheEndOfMonth() {
        if (LocalDate.now().equals(YearMonth.now().atEndOfMonth())) {
            bankAccountService.fetchAllBalances();
        }
    }
}