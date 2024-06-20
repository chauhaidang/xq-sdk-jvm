package com.xq.accounts.controller;

import com.xq.accounts.constants.AccountsContants;
import com.xq.accounts.dto.CustomerDto;
import com.xq.accounts.dto.ResponseDto;
import com.xq.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Slf4j
@AllArgsConstructor
public class AccountsController {
    IAccountsService accountService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {
        log.info("Request received to create account for customer: {}", customerDto.toString());
        accountService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsContants.STATUS_201, AccountsContants.MSG_201));
    }
}
