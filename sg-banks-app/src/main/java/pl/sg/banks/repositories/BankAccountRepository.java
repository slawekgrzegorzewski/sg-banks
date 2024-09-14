package pl.sg.banks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.banks.model.BankAccount;
import pl.sg.banks.model.BankPermission;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    BankAccount getBankAccountByExternalId(String externalId);

    @Query("SELECT ba FROM BankAccount ba WHERE ba.bankPermission.withdrawnAt IS NULL")
    List<BankAccount> findBankAccountsToFetchData();

    @Query("SELECT ba FROM BankAccount ba WHERE ba.bankPermission = ?1")
    List<BankAccount> findBankAccountsForPermission(BankPermission bp);
}