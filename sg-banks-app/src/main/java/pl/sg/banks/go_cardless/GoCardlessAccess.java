package pl.sg.banks.go_cardless;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "nodrigen_access")
public class GoCardlessAccess {
    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String accessToken;
    private LocalDateTime accessExpiresAt;
    @Column(columnDefinition = "TEXT")
    private String refreshToken;
    private LocalDateTime refreshExpiresAt;
    private LocalDateTime archivedAt;

    public Integer getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public GoCardlessAccess setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public LocalDateTime getAccessExpiresAt() {
        return accessExpiresAt;
    }

    public GoCardlessAccess setAccessExpiresAt(LocalDateTime accessExpiresAt) {
        this.accessExpiresAt = accessExpiresAt;
        return this;
    }

    public Optional<String> getRefreshToken() {
        return Optional.ofNullable(refreshToken);
    }

    public GoCardlessAccess setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public Optional<LocalDateTime> getRefreshExpiresAt() {
        return Optional.ofNullable(refreshExpiresAt);
    }

    public GoCardlessAccess setRefreshExpiresAt(LocalDateTime refreshExpiresAt) {
        this.refreshExpiresAt = refreshExpiresAt;
        return this;
    }

    public Optional<LocalDateTime> getArchivedAt() {
        return Optional.ofNullable(archivedAt);
    }

    public GoCardlessAccess setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
        return this;
    }
}
