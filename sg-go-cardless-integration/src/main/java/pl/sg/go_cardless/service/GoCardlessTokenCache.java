package pl.sg.go_cardless.service;

import java.util.Optional;

public interface GoCardlessTokenCache {

    Optional<GoCardlessToken> get();

    void put(GoCardlessToken token);
}
