package pl.sg.banks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.banks.model.Transaction;

import java.util.Collection;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByTransactionIdIn(Collection<String> transactionIds);
}