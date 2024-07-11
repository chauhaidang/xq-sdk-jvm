package com.xq.accounts.component;

import com.intuit.karate.Http;
import com.intuit.karate.Json;
import com.intuit.karate.Match;
import com.intuit.karate.http.Response;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountsApiTest {

    String createEndpoint = "http://localhost:8080/api/accounts/create";
    String fetchEndpoint = "http://localhost:8080/api/accounts/fetch";
    String updateEndpoint = "http://localhost:8080/api/accounts/update";

    private int randomNumber() {
        return (int) (Math.random() * 100000000);
    }

    @Test
    void testCreateNewAccount() throws Exception {
        Json reqPayload = Json.object();
        String uuid = UUID.randomUUID().toString();
        int randomNumber = randomNumber();
        reqPayload.set("name", uuid);
        reqPayload.set("email", String.format("%s@email.com", uuid));
        reqPayload.set("mobileNumber", randomNumber + "");

        Response res = Http.to(createEndpoint).postJson(reqPayload.toString());
        assertEquals(201, res.getStatus());
        Match.that(res.getBodyConverted()).isEqualTo("{ statusCode: '201', statusMsg: 'Account created successfully' }");
    }

    @Test
    void testCanNotCreateExistingAccount() throws Exception {
        Json reqPayload = Json.object();
        String uuid = UUID.randomUUID().toString();
        int randomNumber = randomNumber();
        reqPayload.set("name", uuid);
        reqPayload.set("email", String.format("%s@email.com", uuid));
        reqPayload.set("mobileNumber", randomNumber + "");

        Http.to(createEndpoint).postJson(reqPayload.toString());
        Response res = Http.to(createEndpoint)
                .postJson(reqPayload.toString());
        Match.that(res.getStatus()).isEqualTo(400);
    }

    @Test
    void testCanUpdateAccountByAccountNumber() {
        Json reqPayload = Json.object();
        String uuid = UUID.randomUUID().toString();
        int randomNumber = randomNumber();
        reqPayload.set("name", uuid);
        reqPayload.set("email", String.format("%s@email.com", uuid));
        reqPayload.set("mobileNumber", randomNumber + "");

        //Create an account
        Http.to(createEndpoint).postJson(reqPayload.toString());
        Response res = Http.to(fetchEndpoint).param("mobileNumber", randomNumber + "").get();
        Json resAccount = Json.of(res.getBodyConverted());

        //Update an account with newly created account
        String newName = UUID.randomUUID().toString();
        int newNumber = randomNumber();
        resAccount.set("name", newName);
        resAccount.set("email", newName + "@gmail.com");
        resAccount.set("mobileNumber", newNumber + "");
        resAccount.set("account.accountType", "HomeLoan");
        res = Http.to(updateEndpoint).put(resAccount);
        Match.that(res.getStatus()).isEqualTo(200);
        Match.that(res.getBodyConverted()).isEqualTo("{statusCode: '200', statusMsg: 'Request processed successfully'}");

        //Fetch an account that just got updated
        res = Http.to(fetchEndpoint).param("mobileNumber", newNumber + "").get();
        Match.that(res.getBodyConverted()).isEqualTo(resAccount.asMap());
    }

    @Test
    void testCanNotUpdateIfNotExist() {
        Json reqPayload = Json.object();
        String newName = UUID.randomUUID().toString();
        int newNumber = randomNumber();
        reqPayload.set("name", newName);
        reqPayload.set("email", newName + "@gmail.com");
        reqPayload.set("mobileNumber", newNumber + "");
        reqPayload.set("account.accountType", "HomeLoan");
        reqPayload.set("account.accountNumber", "1234512309");
        reqPayload.set("account.branchAddress", "124 Bourke Street");

        Response res = Http.to(updateEndpoint).put(reqPayload);
        Match.that(res.getStatus()).isEqualTo(404);
        Match.that(
                res.getBodyConverted())
                .contains(String.format("{ errorCode: NOT_FOUND, errorMessage:Account not found with the given input data AccountNumber:'%s' }", "1234512309"));

    }
}
