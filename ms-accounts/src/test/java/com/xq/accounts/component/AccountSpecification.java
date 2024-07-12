package com.xq.accounts.component;

import com.intuit.karate.Http;
import com.intuit.karate.Json;
import com.intuit.karate.http.Response;

import java.util.UUID;

public class AccountSpecification {
    String createEndpoint = "http://localhost:8080/api/accounts/create";
    public static int randomNumber() {
        return (int) (Math.random() * 100000000);
    }

    public TestDto.AccountResponse createNew() {
        String name = UUID.randomUUID().toString();
        TestDto.AccountResponse account = new TestDto.AccountResponse(
                name,
                String.format("%s@email.com", name),
                randomNumber() + "",
                null, null
        );

        Json body = Json.object();
        body.set("name", account.name());
        body.set("email", account.email());
        body.set("mobileNumber", account.mobileNumber());

        Response res = Http.to(createEndpoint).postJson(body.toString());
        return new TestDto.AccountResponse(account.name(), account.email(), account.mobileNumber(), body, res);
    }
}
