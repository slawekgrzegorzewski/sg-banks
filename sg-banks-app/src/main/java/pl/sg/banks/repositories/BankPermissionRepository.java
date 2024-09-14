package pl.sg.banks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.banks.model.BankAccount;
import pl.sg.banks.model.BankPermission;

import java.util.List;
import java.util.Optional;

public interface BankPermissionRepository extends JpaRepository<BankPermission, Integer> {

    @Query("SELECT bp FROM BankPermission bp WHERE bp.givenAt IS NULL and bp.withdrawnAt IS NULL")
    List<BankPermission> findPermissionsToConfirm();

    @Query("SELECT bp FROM BankPermission bp WHERE bp.givenAt IS NOT NULL and bp.withdrawnAt IS NULL")
    List<BankPermission> findPermissionsGranted();

    @Query("SELECT bp FROM BankPermission bp WHERE bp.givenAt IS NOT NULL " +
            "AND bp.withdrawnAt IS NULL " +
            "AND ?1 MEMBER OF bp.bankAccounts")
    Optional<BankPermission> getPermissionsGrantedForBankAccount(BankAccount bankAccount);

    @Query("SELECT bp FROM BankPermission bp " +
            "WHERE bp.givenAt IS NULL and bp.withdrawnAt IS NULL AND bp.reference = ?1")
    Optional<BankPermission> findPermissionToConfirm(String reference);
}
