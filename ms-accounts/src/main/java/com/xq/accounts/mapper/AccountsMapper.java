package com.xq.accounts.mapper;

import com.xq.accounts.dto.AccountsDto;
import com.xq.accounts.entity.Accounts;

public class AccountsMapper {
    public static AccountsDto mapToAccountsDto(Accounts accounts, AccountsDto accountDto) {
        accountDto.setAccountNumber(accounts.getAccountNumber());
        accountDto.setAccountType(accounts.getAccountType());
        accountDto.setBranchAddress(accounts.getBranchAddress());
        return accountDto;
    }

    public static Accounts mapToAccounts(AccountsDto accountsDto, Accounts account) {
        account.setAccountNumber(accountsDto.getAccountNumber());
        account.setAccountType(accountsDto.getAccountType());
        account.setBranchAddress(accountsDto.getBranchAddress());
        return account;
    }
}
