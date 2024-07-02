package com.xq.accounts.component;

import com.intuit.karate.junit5.Karate;

public class AccountsTest {
    @Karate.Test
    Karate testAccountsApi() {
       return Karate.run("accounts").relativeTo(getClass());
    }
}
