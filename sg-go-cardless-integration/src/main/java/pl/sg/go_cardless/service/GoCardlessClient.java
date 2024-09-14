package pl.sg.go_cardless.service;

import io.netty.handler.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import pl.sg.go_cardless.rest.*;
import pl.sg.go_cardless.rest.balances.BalancesMain;
import pl.sg.go_cardless.rest.transactions.TransactionsMain;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

public class GoCardlessClient {

    private static final Logger LOG = LoggerFactory.getLogger(GoCardlessClient.class);
    private final GoCardlessTokenCache goCardlessTokenCache;
    private final String goCardlessUrl;
    private final String goCardlessSecretId;
    private final String goCardlessSecretKey;
    private final Supplier<LocalDateTime> nowSupplier;


    public GoCardlessClient(
            GoCardlessTokenCache goCardlessTokenCache,
            String goCardlessUrl,
            String goCardlessSecretId,
            String goCardlessSecretKey,
            Supplier<LocalDateTime> nowSupplier) {
        this.goCardlessTokenCache = goCardlessTokenCache;
        this.goCardlessUrl = goCardlessUrl;
        this.goCardlessSecretId = goCardlessSecretId;
        this.goCardlessSecretKey = goCardlessSecretKey;
        this.nowSupplier = nowSupplier;
    }

    public List<Institution> listInstitutions(String country) {
        return createClient().get()
                .uri("institutions/", Map.of("country", country))
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .onRawStatus(statusCode -> statusCode != 200, clientResponse -> {
                    LOG.warn("Problem with - list institutions " + country);
                    return Mono.empty();
                })
                .bodyToFlux(Institution.class)
                .collectList()
                .block();
    }

    public Institution getInstitution(String institutionId) {
        return createClient().get()
                .uri("institutions/" + institutionId + "/")
                .header("Authorization", "Bearer " + getToken())
                .retrieve()
                .onRawStatus(statusCode -> statusCode != 200, clientResponse -> {
                    LOG.warn("Problem with - get institution " + institutionId);
                    return Mono.empty();
                })
                .bodyToMono(Institution.class)
                .block();
    }

    public Optional<AgreementCreationResponse> createAgreement(GoCardlessPermissionRequest goCardlessPermissionRequest) {
        AgreementCreationRequest agreementCreationRequest = new AgreementCreationRequest(
                goCardlessPermissionRequest.getMaxHistoricalDays(),
                90,
                new String[]{"balances", "details", "transactions"},
                goCardlessPermissionRequest.getInstitutionId());
        return ofNullable(
                createClient()
                        .post()
                        .uri("agreements/enduser/")
                        .header("Authorization", "Bearer " + getToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(agreementCreationRequest)
                        .retrieve()
                        .onRawStatus(statusCode -> statusCode != 200, clientResponse -> {
                            LOG.warn("Problem with create agreement " + agreementCreationRequest);
                            return Mono.empty();
                        })
                        .bodyToMono(AgreementCreationResponse.class)
                        .block()
        );

    }

    public Optional<Requisition> createRequisition(GoCardlessPermissionRequest goCardlessPermissionRequest, UUID requisitionId, String reference) {
        RequisitionCreationRequest requisitionCreationRequest = new RequisitionCreationRequest(
                goCardlessPermissionRequest.getRedirect(),
                goCardlessPermissionRequest.getInstitutionId(),
                requisitionId,
                reference,
                goCardlessPermissionRequest.getUserLanguage(),
                false,
                false);
        return ofNullable(
                createClient()
                        .post()
                        .uri("requisitions/")
                        .header("Authorization", "Bearer " + getToken())
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(requisitionCreationRequest)
                        .retrieve()
                        .onRawStatus(statusCode -> statusCode != 200, clientResponse -> {
                            LOG.warn("Problem with create requisition " + requisitionCreationRequest);
                            return Mono.empty();
                        })
                        .bodyToMono(Requisition.class)
                        .block()
        );
    }

    public Optional<Requisition> getRequisition(UUID requisitionId) {
        return ofNullable(
                createClient()
                        .get()
                        .uri("requisitions/" + requisitionId + "/")
                        .header("Authorization", "Bearer " + getToken())
                        .retrieve()
                        .onRawStatus(statusCode -> statusCode != 200, clientResponse -> {
                            LOG.warn("Problem with create requisituion " + requisitionId);
                            return Mono.empty();
                        })
                        .bodyToMono(Requisition.class)
                        .block()
        );
    }

    public Optional<AccountDetails> getAccountDetails(UUID bankAccountId) {
        return getAccountData(
                bankAccountId,
                "accounts/" + bankAccountId + "/details/",
                "get account details",
                AccountDetails.class);
    }

    public Optional<TransactionsMain> getTransactions(UUID bankAccountId) {
        return getAccountData(
                bankAccountId,
                "accounts/" + bankAccountId + "/transactions/",
                "get transactions",
                TransactionsMain.class);
    }

    public Optional<BalancesMain> getBalances(UUID bankAccountId) {
        return getAccountData(
                bankAccountId,
                "accounts/" + bankAccountId + "/balances/",
                "get balances",
                BalancesMain.class);
    }

    private <T> Optional<T> getAccountData(UUID bankAccountId, String uri, String description, Class<T> resultClass) {
        return ofNullable(
                createClient()
                        .get()
                        .uri(uri)
                        .header("Authorization", "Bearer " + getToken())
                        .retrieve()
                        .onRawStatus(
                                statusCode -> statusCode == 400,
                                clientResponse -> clientResponse.bodyToMono(String.class)
                                        .filter(GoCardlessClient::isEUAEndedException)
                                        .map(body -> new EUAEndedException())
                        )
                        .onRawStatus(
                                statusCode -> statusCode / 100 != 2,
                                clientResponse -> Mono.empty())
                        .bodyToMono(resultClass)
                        .block()
        );
    }

    private static boolean isEUAEndedException(String body) {
        return Stream.of("EUA was valid for", "and it expired at", "The end user must connect the account once more with new EUA and Requisition").allMatch(body::contains);
    }

    private synchronized String getToken() {
        LocalDateTime now = nowSupplier.get();
        GoCardlessToken goCardlessToken = goCardlessTokenCache.get().orElse(null);
        if (!tokenIsValid(goCardlessToken, now)) {
            TokenResponse tokenResponse = (goCardlessToken == null || !goCardlessToken.isRefreshTokenValid(now))
                    ? getNewToken()
                    : refreshToken(goCardlessToken).orElseGet(this::getNewToken);
            goCardlessToken = new GoCardlessToken(
                    Objects.requireNonNull(tokenResponse).getAccess(),
                    now.plusSeconds(tokenResponse.getAccessExpires()),
                    tokenResponse.getRefresh(),
                    now.plusSeconds(tokenResponse.getRefreshExpires()));
            goCardlessTokenCache.put(goCardlessToken);
        }
        return goCardlessToken.accessToken();
    }

    private boolean tokenIsValid(GoCardlessToken token, LocalDateTime now) {
        return token != null && token.isAccessTokenValid(now);
    }

    private TokenResponse getNewToken() {
        return createClient().post()
                .uri("token/new/")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new NewTokenRequest(goCardlessSecretId, goCardlessSecretKey))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }

    private Optional<TokenResponse> refreshToken(GoCardlessToken goCardlessToken) {
        return ofNullable(createClient().post()
                .uri("token/refresh/")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new RefreshTokenRequest(goCardlessToken.refreshToken()))
                .retrieve()
                .onRawStatus(statusCode -> statusCode / 100 != 2, clientResponse -> {
                    LOG.info("Couldn't refresh token");
                    return Mono.empty();
                })
                .bodyToMono(TokenResponse.class)
                .block());
    }

    private WebClient createClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .wiretap(
                                "go-cardless-request",
                                LogLevel.INFO,
                                AdvancedByteBufFormat.TEXTUAL)))
                .baseUrl(goCardlessUrl)
                .build();
    }

}
