package pl.sg.banks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.banks.model.BankAccountBalance;

public interface BankAccountBalanceRepository extends JpaRepository<BankAccountBalance, Integer> {
}