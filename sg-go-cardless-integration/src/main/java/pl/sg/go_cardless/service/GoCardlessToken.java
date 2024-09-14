package pl.sg.go_cardless.service;

import java.time.LocalDateTime;

public record GoCardlessToken(String accessToken, LocalDateTime accessTokenExpiresAt, String refreshToken, LocalDateTime refreshTokenExpiresAt) {

    public boolean isAccessTokenValid(LocalDateTime now) {
        return now.isBefore(accessTokenExpiresAt);
    }

    public boolean isRefreshTokenValid(LocalDateTime now) {
        return now.isBefore(refreshTokenExpiresAt);
    }
}
