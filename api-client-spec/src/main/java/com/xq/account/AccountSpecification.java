package com.xq.account;

import com.intuit.karate.Http;
import com.intuit.karate.Json;
import com.intuit.karate.http.Response;
import com.xq.ConfigReader;
import com.xq.Dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AccountSpecification {
    private String name;
    private String email;
    private String mobileNumber;
    private String endpoint = new ConfigReader().loadConfig().getApiGateway();

    public String useRandomMobileNumber() {
        mobileNumber = ((int) ((Math.random() * 9000000000L) + 1000000000L)) + "";
        return mobileNumber;
    }

    public Dto.Account createAccount() {
        Dto.Account account = new Dto.Account(name, email, mobileNumber, null, null);
        Json body = Json.object();
        body.set("name", account.name());
        body.set("email", account.email());
        body.set("mobileNumber", account.mobileNumber());
        Response res = Http.to(endpoint + "/api/accounts/create").postJson(body.toString());
        return new Dto.Account(account.name(), account.email(), account.mobileNumber(), body, res);
    }
}
