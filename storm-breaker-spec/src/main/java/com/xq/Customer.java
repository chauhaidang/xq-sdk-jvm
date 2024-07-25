package com.xq;

import com.xq.account.CreatingDefaultAccount;

public class Customer {
    public static Dto.Account onboardNewAccountBy(CreatingDefaultAccount creatingDefaultAccount) {
       return creatingDefaultAccount.execute();
    }
}
