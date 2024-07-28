package com.xq.accounts.component;

import com.intuit.karate.Http;
import com.intuit.karate.Json;
import com.intuit.karate.Match;
import com.intuit.karate.http.Response;
import com.xq.Customer;
import com.xq.Dto;
import com.xq.account.AccountSpecification;
import com.xq.account.CreatingDefaultAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountsApiTest {

    String fetchEndpoint = "http://localhost:8080/api/accounts/fetch";
    String updateEndpoint = "http://localhost:8080/api/accounts/update";
    String deleteEndpoint = "http://localhost:8080/api/accounts/delete";
    final String expectedEmailValidationMsg = "Email address must be a valid email";

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

    @ParameterizedTest
    @ValueSource(strings = {"044466666", "04446666666"})
    void testCanNotCreateAccountWithInvalidMobileNumber(String mobileNumber) {
        CreatingDefaultAccount creatingDefaultAccount = CreatingDefaultAccount.withDefaultType();
        var specInvalidMobileNo = creatingDefaultAccount.getSpecification();
        specInvalidMobileNo.setMobileNumber(mobileNumber);
        var accountRes = Customer.onboardNewAccountBy(CreatingDefaultAccount.withSpecification(specInvalidMobileNo)).resBody();

        Match.that(accountRes.getStatus()).isEqualTo(400);
        Match.that(accountRes.getBodyConverted()).isEqualTo("{mobileNumber: 'Mobile number must be 10 digits'}");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd", "123456789012345678901234567890*"})
    void testCanNotCreateAccountWithInvalidName(String name) {
        CreatingDefaultAccount creatingDefaultAccount = CreatingDefaultAccount.withDefaultType();
        var specInvalidName = creatingDefaultAccount.getSpecification();
        specInvalidName.setName(name);
        var accountRes = Customer.onboardNewAccountBy(CreatingDefaultAccount.withSpecification(specInvalidName)).resBody();

        Match.that(accountRes.getStatus()).isEqualTo(400);
        Match.that(accountRes.getBodyConverted())
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
        var accountRes = Customer.onboardNewAccountBy(CreatingDefaultAccount.withSpecification(specInvalidEmail)).resBody();

        Match.that(accountRes.getStatus()).isEqualTo(400);
        Match.that(accountRes.getBodyConverted())
                .isEqualTo(String.format("{email:'%s'}", validationMsg));
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
