package pl.sg.banks.go_cardless;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import org.springframework.stereotype.Component;
import pl.sg.go_cardless.service.GoCardlessToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class GoCardlessTokenCache implements pl.sg.go_cardless.service.GoCardlessTokenCache {

    private final GoCardlessAccessRepository goCardlessAccessRepository;

    public GoCardlessTokenCache(GoCardlessAccessRepository goCardlessAccessRepository) {
        this.goCardlessAccessRepository = goCardlessAccessRepository;
    }

    @Override
    public synchronized Optional<GoCardlessToken> get() {
        return goCardlessAccessRepository.getAccess().map(
                access -> new GoCardlessToken(
                        access.getAccessToken(),
                        access.getAccessExpiresAt(),
                        access.getRefreshToken().orElse(""),
                        access.getRefreshExpiresAt().orElse(LocalDateTime.MIN)
                )
        );
    }

    @Override
    public synchronized void put(GoCardlessToken token) {
        goCardlessAccessRepository.archiveAll(LocalDateTime.now());
        goCardlessAccessRepository.save(new GoCardlessAccess()
                .setAccessToken(token.accessToken())
                .setAccessExpiresAt(token.accessTokenExpiresAt())
                .setRefreshToken(token.refreshToken())
                .setRefreshExpiresAt(token.accessTokenExpiresAt()));
    }
}
