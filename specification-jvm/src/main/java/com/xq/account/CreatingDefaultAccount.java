package com.xq.account;

import com.xq.Dto;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreatingDefaultAccount implements AccountCommand {
    private final AccountSpecification specification;

    private CreatingDefaultAccount(AccountSpecification accountSpecification) {
        this.specification = accountSpecification;
    }

    public static CreatingDefaultAccount withDefaultType() {
        String name = UUID.randomUUID().toString();
        AccountSpecification accountSpecification = new AccountSpecification();
        accountSpecification.useRandomMobileNumber();
        accountSpecification.setName(name);
        accountSpecification.setEmail(String.format("%s@email.com", name));

        return new CreatingDefaultAccount(accountSpecification);
    }

    public static CreatingDefaultAccount withSpecification(AccountSpecification accountSpecification) {
        return new CreatingDefaultAccount(accountSpecification);
    }

    @Override
    public Dto.Account execute() {
        return specification.createAccount();
    }
}
