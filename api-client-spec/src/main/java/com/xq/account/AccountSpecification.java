package com.xq.account;

import com.intuit.karate.Http;
import com.intuit.karate.Json;
import com.intuit.karate.http.Response;
import com.xq.ConfigReader;
import com.xq.Dto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Random;

@NoArgsConstructor
@Data
public class AccountSpecification {
    private static final ThreadLocal<RequestSpecification> requestSpecification = new ThreadLocal<>();

    public static void setSpec(RequestSpecification spec) {
        requestSpecification.set(spec);
    }

    public static RequestSpecification getRequestSpecification() {
        return requestSpecification.get();
    }

    private String name;
    private String email;
    private String mobileNumber;
    private String endpoint = new ConfigReader().loadConfig().getApiGateway();

    public void useRandomMobileNumber() {
        SecureRandom random = new SecureRandom();
        Long randomNum = random.nextLong(9000000000L) + 1000000000L;
        this.mobileNumber = String.valueOf(randomNum);
    }

    public Dto.AccountJson createAccount() {
        Dto.Account account = new Dto.Account(name, email, mobileNumber, null, null);
        Json body = Json.object();
        body.set("name", account.name());
        body.set("email", account.email());
        body.set("mobileNumber", account.mobileNumber());

        setSpec(null);
        setSpec(
                RestAssured.given().baseUri(endpoint).basePath("/api/accounts/create")
                        .contentType(ContentType.JSON).body(body.toString()).log().all()
        );
        ValidatableResponse res = getRequestSpecification()
                .post().then().log().all();
        return new Dto.AccountJson(
                account.name(), account.email(), account.mobileNumber(), body, Json.of(res.extract().response().asString()), res.extract().statusCode());
    }
}
