package com.xq;

import com.xq.account.AccountCommand;

public class Customer {
    public static Dto.Account onboardNewAccountBy(AccountCommand accountCommand) {
       return accountCommand.execute();
    }
}
