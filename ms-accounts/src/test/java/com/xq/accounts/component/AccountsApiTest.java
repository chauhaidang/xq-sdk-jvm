package com.xq.accounts.component;

import com.intuit.karate.Http;
import com.intuit.karate.Json;
import com.intuit.karate.Match;
import com.intuit.karate.http.Response;
import com.xq.Customer;
import com.xq.Dto;
import com.xq.account.CreatingDefaultAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountsApiTest {

    String fetchEndpoint = "http://localhost:8080/api/accounts/fetch";
    String updateEndpoint = "http://localhost:8080/api/accounts/update";
    String deleteEndpoint = "http://localhost:8080/api/accounts/delete";

    @Test()
    void testCreateNewAccount() throws Exception {
        Dto.Account account = Customer.onboardNewAccountBy(CreatingDefaultAccount.withDefaultType());
        assertEquals(201, account.resBody().getStatus());
        Match.that(account.resBody().getBodyConverted()).isEqualTo("{ statusCode: '201', statusMsg: 'Account created successfully' }");
    }

    @Test
    void testCanNotCreateExistingAccount() throws Exception {
        CreatingDefaultAccount creatingDefaultAccount = CreatingDefaultAccount.withDefaultType();
        Customer.onboardNewAccountBy(creatingDefaultAccount);
        Dto.Account account = Customer.onboardNewAccountBy(CreatingDefaultAccount.withSpecification(creatingDefaultAccount.getSpecification()));
        Match.that(account.resBody().getStatus()).isEqualTo(400);
    }

    @Test
    void testCanUpdateAccountByAccountNumber() {
        Dto.Account account = Customer.onboardNewAccountBy(CreatingDefaultAccount.withDefaultType());
        Response res = Http.to(fetchEndpoint).param("mobileNumber", account.mobileNumber()).get();
        Json resAccount = Json.of(res.getBodyConverted());

        //Update an account with newly created account
        String newName = CreatingDefaultAccount.withDefaultType().getSpecification().getName();
        String newNumber = CreatingDefaultAccount.withDefaultType().getSpecification().getMobileNumber();
        resAccount.set("name", newName);
        resAccount.set("email", newName + "@gmail.com");
        resAccount.set("mobileNumber", newNumber);
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
        String newName = CreatingDefaultAccount.withDefaultType().getSpecification().getName();
        String newNumber = CreatingDefaultAccount.withDefaultType().getSpecification().getMobileNumber();
        reqPayload.set("name", newName);
        reqPayload.set("email", newName + "@gmail.com");
        reqPayload.set("mobileNumber", newNumber);
        reqPayload.set("account.accountType", "HomeLoan");
        reqPayload.set("account.accountNumber", "1234512309");
        reqPayload.set("account.branchAddress", "124 Bourke Street");

        Response res = Http.to(updateEndpoint).put(reqPayload);
        Match.that(res.getStatus()).isEqualTo(404);
        Match.that(res.getBodyConverted())
                .contains(String.format(
                        "{ errorCode: NOT_FOUND, errorMessage:Account not found with the given input data AccountNumber:'%s' }", "1234512309"));

    }

    @Test
    void testDeleteAnAccountByMobileNumber() {
        Dto.Account account = Customer.onboardNewAccountBy(CreatingDefaultAccount.withDefaultType());
        Response res = Http.to(deleteEndpoint).param("mobileNumber", account.mobileNumber()).delete();
        assertEquals(200, res.getStatus());
        Match.that(res.getBodyConverted()).isEqualTo("{ statusCode: '200', statusMsg: 'Account deleted successfully' }");
    }

    @Test
    void testCanNotDeleteAnAccountIfNotExist() {
        Response res = Http.to(deleteEndpoint).param("mobileNumber", "123456789").delete();
        assertEquals(404, res.getStatus());
        Match.that(res.getBodyConverted())
                .contains(String.format(
                        "{ errorCode: NOT_FOUND, errorMessage:Customer not found with the given input data mobileNumber:'%s' }", "123456789"));
    }
}
