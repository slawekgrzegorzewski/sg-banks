package pl.sg.go_cardless;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.sg.go_cardless.rest.*;
import pl.sg.go_cardless.rest.balances.BalancesMain;
import pl.sg.go_cardless.rest.transactions.TransactionsMain;
import pl.sg.go_cardless.service.GoCardlessClient;
import pl.sg.go_cardless.service.GoCardlessToken;
import pl.sg.go_cardless.service.GoCardlessTokenCache;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

class GoCardlessClientTest {

    @Test
    @Disabled
    void a() throws MalformedURLException {
        GoCardlessClient goCardlessClient = goCardlessClient();
        List<Institution> institutions = goCardlessClient.listInstitutions("PL");
        institutions.forEach(institution -> System.out.println("institution = " + institution));

        Institution sandboxfinanceSfin0000 = goCardlessClient.getInstitution("SANDBOXFINANCE_SFIN0000");
        System.out.println("sandboxfinanceSfin0000 = " + sandboxfinanceSfin0000);

        GoCardlessPermissionRequest goCardlessPermissionRequest = new GoCardlessPermissionRequest(
                sandboxfinanceSfin0000.getId(),
                sandboxfinanceSfin0000.getTransactionTotalDays(),
                URI.create("http://localhost").toURL(),
                "pl"
        );
        AgreementCreationResponse goCardlessClientAgreement = goCardlessClient
                .createAgreement(goCardlessPermissionRequest)
                .orElseThrow();
        System.out.println("goCardlessClientAgreement = " + goCardlessClientAgreement);

        Requisition requisition = goCardlessClient.createRequisition(
                goCardlessPermissionRequest,
                goCardlessClientAgreement.getId(),
                String.valueOf(new Random().nextInt())
        ).orElseThrow();
        System.out.println("requisition = " + requisition);

    }

    @Test
    @Disabled
    void b() throws MalformedURLException {
        GoCardlessClient goCardlessClient = goCardlessClient();
        Requisition requisition = goCardlessClient.getRequisition(UUID.fromString("64161a0d-36d6-4005-8e6a-536fda1af400")).orElseThrow();
        System.out.println("requisition = " + requisition);

        for (UUID bankAccountId : requisition.getAccounts()) {
            getBankAccountDetails(goCardlessClient, bankAccountId);
        }
    }

    private static void getBankAccountDetails(GoCardlessClient goCardlessClient, UUID bankAccountId) {
        AccountDetails accountDetails = goCardlessClient.getAccountDetails(bankAccountId).orElseThrow();
        System.out.println("bankAccountId = " + bankAccountId);
        System.out.println("accountDetails = " + accountDetails);

//        TransactionsMain transactionsMain = goCardlessClient.getTransactions(bankAccountId).orElseThrow();
//        System.out.println("transactionsMain = " + transactionsMain);
//
//        BalancesMain balancesMain = goCardlessClient.getBalances(bankAccountId).orElseThrow();
//        System.out.println("balancesMain = " + balancesMain);
    }

    private static GoCardlessClient goCardlessClient() {
        GoCardlessClient goCardlessClient = new GoCardlessClient(
                new GoCardlessTokenCache() {
                    private GoCardlessToken goCardlessToken = null;

                    @Override
                    public synchronized Optional<GoCardlessToken> get() {
                        return Optional.ofNullable(goCardlessToken);
                    }

                    @Override
                    public synchronized void put(GoCardlessToken token) {
                        goCardlessToken = token;
                    }
                },
                "https://bankaccountdata.gocardless.com/api/v2/",
                "secretId",
                "secretKey",
                LocalDateTime::now
        );
        return goCardlessClient;
    }
}