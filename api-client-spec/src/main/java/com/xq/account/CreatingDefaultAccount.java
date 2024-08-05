package com.xq.account;

import com.xq.Dto;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.Random;

@Getter
public class CreatingDefaultAccount implements AccountCommand {
    private final AccountSpecification specification;

    private CreatingDefaultAccount(AccountSpecification accountSpecification) {
        this.specification = accountSpecification;
    }

    private static String generateRandomString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static CreatingDefaultAccount withDefaultType() {
        int stringLength = new SecureRandom().nextInt(26) + 5; // Random length between 5 and 30
        String name = generateRandomString(stringLength);
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
    public Dto.AccountJson execute() {
        return specification.createAccount();
    }
}
