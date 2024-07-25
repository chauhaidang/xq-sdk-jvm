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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
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

    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccount(@RequestParam String mobileNumber) {
        log.info("Request received to fetch account details for mobile number: {}", mobileNumber);
        CustomerDto customerDto = accountService.fetchAccount(mobileNumber);
        return ResponseEntity.ok(customerDto);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount(@RequestBody CustomerDto customerDto) {
        log.info("Request received to update account details for customer with mobileNumber: {}",
                customerDto.getAccount().getAccountNumber().toString());
        boolean isUpdated = accountService.updateAccount(customerDto);
        return isUpdated ?
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ResponseDto(AccountsContants.STATUS_200, AccountsContants.MSG_200)) :
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDto(AccountsContants.STATUS_500, AccountsContants.MSG_500));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam String mobileNumber) {
        log.info("Request received to delete account details for mobile number: {}", mobileNumber);
        boolean isDeleted = accountService.deleteAccount(mobileNumber);
        return isDeleted ?
                ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ResponseDto(AccountsContants.STATUS_200, AccountsContants.MSG_DEL_ACCOUNT_SUCCESS)) :
                ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDto(AccountsContants.STATUS_500, AccountsContants.MSG_500));    }
}
