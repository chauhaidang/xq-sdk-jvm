package com.xq.account;

import com.xq.Dto;

import java.util.UUID;

public class CreatingDefaultAccount implements AccountCommand {
    private final AccountSpecification specification;

    private CreatingDefaultAccount(AccountSpecification accountSpecification) {
        this.specification = accountSpecification;
    }

    public static CreatingDefaultAccount withDefaultType() {
        String name = UUID.randomUUID().toString();
        String mobileNumber = ((int) (Math.random() * 100000000)) + "";
        AccountSpecification accountSpecification = new AccountSpecification();
        accountSpecification.setName(name);
        accountSpecification.setEmail(String.format("%s@email.com", name));
        accountSpecification.setMobileNumber(mobileNumber);

        return new CreatingDefaultAccount(accountSpecification);
    }

    @Override
    public Dto.Account execute() {
        return specification.createAccount();
    }
}
