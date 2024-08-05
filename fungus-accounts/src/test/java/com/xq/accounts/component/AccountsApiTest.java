package com.xq.accounts.component;

import com.intuit.karate.Json;
import com.intuit.karate.Match;
import com.xq.Customer;
import com.xq.Dto;
import com.xq.account.AccountSpecification;
import com.xq.account.CreatingDefaultAccount;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.xq.account.AccountSpecification.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountsApiTest {

    String fetchEndpoint = "/api/accounts/fetch";
    String updateEndpoint = "/api/accounts/update";
    String deleteEndpoint = "/api/accounts/delete";
    String baseURL = "http://localhost:8080";
    final String expectedEmailValidationMsg = "Email address must be a valid email";

    @BeforeEach
    void before() {
        AccountSpecification.setSpec(null);
    }

    @Test()
    void testCreateNewAccount() throws Exception {
        Dto.AccountJson account = Customer.onboardNewAccountBy(CreatingDefaultAccount.withDefaultType());
        assertEquals(201, account.statusCode());
        Match.that(account.resBody().toString()).isEqualTo("{ statusCode: '201', statusMsg: Account created successfully }");
    }

    @Test
    void testCanNotCreateExistingAccount() throws Exception {
        CreatingDefaultAccount creatingDefaultAccount = CreatingDefaultAccount.withDefaultType();
        Customer.onboardNewAccountBy(creatingDefaultAccount);
        Dto.AccountJson account = Customer.onboardNewAccountBy(CreatingDefaultAccount.withSpecification(creatingDefaultAccount.getSpecification()));
        Match.that(account.statusCode()).isEqualTo(400);
    }

    @ParameterizedTest
    @ValueSource(strings = {"044466666", "04446666666"})
    void testCanNotCreateAccountWithInvalidMobileNumber(String mobileNumber) {
        CreatingDefaultAccount creatingDefaultAccount = CreatingDefaultAccount.withDefaultType();
        var specInvalidMobileNo = creatingDefaultAccount.getSpecification();
        specInvalidMobileNo.setMobileNumber(mobileNumber);
        var accountRes = Customer.onboardNewAccountBy(CreatingDefaultAccount.withSpecification(specInvalidMobileNo));

        Match.that(accountRes.statusCode()).isEqualTo(400);
        Match.that(accountRes.resBody().toString()).isEqualTo("{mobileNumber: 'Mobile number must be 10 digits'}");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd", "123456789012345678901234567890*"})
    void testCanNotCreateAccountWithInvalidName(String name) {
        CreatingDefaultAccount creatingDefaultAccount = CreatingDefaultAccount.withDefaultType();
        var specInvalidName = creatingDefaultAccount.getSpecification();
        specInvalidName.setName(name);
        var accountRes = Customer.onboardNewAccountBy(CreatingDefaultAccount.withSpecification(specInvalidName));

        Match.that(accountRes.statusCode()).isEqualTo(400);
        Match.that(accountRes.resBody().toString())
                .isEqualTo(
                        "{name: 'Customer name length must be greater or equal to 5 and less than or equal to 30'}");
    }

    @ParameterizedTest
    @CsvSource(
            value = {
                    "user@|" + expectedEmailValidationMsg,
                    "@domain.com|" + expectedEmailValidationMsg,
                    "user.user@domain..com|" + expectedEmailValidationMsg,
                    "user user@domain.com|" + expectedEmailValidationMsg,
                    "user.@domain.com|" + expectedEmailValidationMsg,
                    ".user@domain.com|" + expectedEmailValidationMsg,
                    "|" + "Email address can not be null or empty",
            }, delimiter = '|'
    )
    void testCanNotCreateAccountWithInvalidEmail(String email, String validationMsg) {
        CreatingDefaultAccount creatingDefaultAccount = CreatingDefaultAccount.withDefaultType();
        var specInvalidEmail = creatingDefaultAccount.getSpecification();
        specInvalidEmail.setEmail(email);
        var accountRes = Customer.onboardNewAccountBy(CreatingDefaultAccount.withSpecification(specInvalidEmail));

        Match.that(accountRes.statusCode()).isEqualTo(400);
        Match.that(accountRes.resBody().toString())
                .isEqualTo(String.format("{email:'%s'}", validationMsg));
    }

    @Test
    void testCanUpdateAccountByAccountNumber() {
        Dto.AccountJson account = Customer.onboardNewAccountBy(CreatingDefaultAccount.withDefaultType());
        setSpec(
                RestAssured.given().baseUri(baseURL).basePath(fetchEndpoint).queryParam("mobileNumber", account.mobileNumber())
                        .contentType(ContentType.JSON).log().all()
        );
        var res = getRequestSpecification().get().then().log().all().statusCode(200).extract().body().asString();
        Json resAccount = Json.of(res);

        //Update an account with newly created account
        String newName = CreatingDefaultAccount.withDefaultType().getSpecification().getName();
        String newNumber = CreatingDefaultAccount.withDefaultType().getSpecification().getMobileNumber();
        resAccount.set("name", newName);
        resAccount.set("email", newName + "@gmail.com");
        resAccount.set("mobileNumber", newNumber);
        resAccount.set("account.accountType", "HomeLoan");
        setSpec(
                RestAssured.given().baseUri(baseURL).basePath(updateEndpoint).body(resAccount.toString())
                        .contentType(ContentType.JSON).log().all()
        );
        var updateAccountRes = getRequestSpecification().put().then().log().all().statusCode(200).extract().body().asString();
        Match.that(updateAccountRes).isEqualTo("{statusCode: '200', statusMsg: 'Request processed successfully'}");

        //Fetch an account that just got updated
        setSpec(
                RestAssured.given().baseUri(baseURL).basePath(fetchEndpoint).queryParam("mobileNumber", newNumber)
                        .contentType(ContentType.JSON).log().all()
        );
        var getAccountRes = getRequestSpecification().get().then().log().all().statusCode(200).extract().body().asString();
        Match.that(getAccountRes).containsDeep(resAccount.toString());
    }

    @Test
    void testCanNotUpdateIfNotExist() {
        Json reqPayload = Json.object();
        String newName = CreatingDefaultAccount.withDefaultType().getSpecification().getName();
        String newNumber = CreatingDefaultAccount.withDefaultType().getSpecification().getMobileNumber();
        reqPayload.set("name", newName);
        reqPayload.set("email", newName + "@gmail.com");
        reqPayload.set("mobileNumber", newNumber);
        reqPayload.set("account.accountType", "HomeLoan");
        reqPayload.set("account.accountNumber", "1234512309");
        reqPayload.set("account.branchAddress", "124 Bourke Street");
        setSpec(
               RestAssured.given().baseUri(baseURL).basePath(updateEndpoint).body(reqPayload.toString())
                       .contentType(ContentType.JSON).log().all()
        );
        var res = getRequestSpecification().put().then().log().all().statusCode(404)
                .extract().body().asString();
        Match.that(res)
                .contains(String.format(
                        "{ errorCode: NOT_FOUND, errorMessage:Account not found with the given input data AccountNumber:'%s' }", "1234512309"));
    }

    @Test
    void testDeleteAnAccountByMobileNumber() {
        Dto.AccountJson account = Customer.onboardNewAccountBy(CreatingDefaultAccount.withDefaultType());
        setSpec(
                RestAssured.given().baseUri(baseURL).basePath(deleteEndpoint).contentType(ContentType.JSON)
                        .queryParam("mobileNumber", account.mobileNumber()).log().all()
        );
        var res = getRequestSpecification().delete()
                .then().log().all().statusCode(200).extract().body().asString();
        Match.that(res).isEqualTo("{ statusCode: '200', statusMsg: 'Account deleted successfully' }");
    }

    @Test
    void testCanNotDeleteAnAccountIfNotExist() {
        setSpec(
                RestAssured.given().baseUri(baseURL).basePath(deleteEndpoint).contentType(ContentType.JSON)
                        .queryParam("mobileNumber", "1234567890").log().all()
        );
        String response = getRequestSpecification().delete()
                .then().log().all().statusCode(404).extract().body().asString();
        Match.that(response)
                .contains(String.format(
                        "{ errorCode: NOT_FOUND, errorMessage:Customer not found with the given input data mobileNumber:'%s' }", "1234567890"));
    }
}
