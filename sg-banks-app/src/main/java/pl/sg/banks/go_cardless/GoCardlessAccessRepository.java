package pl.sg.banks.go_cardless;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface GoCardlessAccessRepository extends JpaRepository<GoCardlessAccess, Integer> {

    @Query("SELECT na FROM GoCardlessAccess na WHERE na.archivedAt IS NULL")
    Optional<GoCardlessAccess> getAccess();

    @Transactional
    @Modifying
    @Query("UPDATE GoCardlessAccess na SET na.archivedAt = ?1 WHERE na.archivedAt IS NULL")
    void archiveAll(LocalDateTime archiveAtTime);

}