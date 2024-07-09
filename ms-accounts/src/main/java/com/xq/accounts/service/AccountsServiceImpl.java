package com.xq.accounts.service;

import com.xq.accounts.constants.AccountsContants;
import com.xq.accounts.dto.AccountsDto;
import com.xq.accounts.dto.CustomerDto;
import com.xq.accounts.entity.Accounts;
import com.xq.accounts.entity.Customer;
import com.xq.accounts.exception.CustomerAlreadyExistsException;
import com.xq.accounts.exception.ResourceNotFoundException;
import com.xq.accounts.mapper.AccountsMapper;
import com.xq.accounts.mapper.CustomerMapper;
import com.xq.accounts.repository.AccountsRepository;
import com.xq.accounts.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class AccountsServiceImpl implements IAccountsService {
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        log.info("Creating account for customer {}", customerDto);
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> customerByMobileNum = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if (customerByMobileNum.isPresent()) {
            throw new CustomerAlreadyExistsException(String.format(
                    "Customer already registered with given mobile number: %s", customerDto.getMobileNumber()));
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy(customerDto.getMobileNumber());
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts account = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccount(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));
        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto account = customerDto.getAccount();
        if (account != null) {
            Accounts accountEntity = accountsRepository.findById(account.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", account.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(account, accountEntity);
            accountEntity = accountsRepository.save(accountEntity);

            Long customerId = accountEntity.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerId", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts accounts = new Accounts();
        accounts.setCustomerId(customer.getCustomerId());

        long randomAccNum = 1000000000L + new Random().nextInt(900000000);
        accounts.setAccountNumber(randomAccNum);
        accounts.setAccountType(AccountsContants.SAVINGS);
        accounts.setBranchAddress(AccountsContants.ADDRESS);
        accounts.setCreatedAt(LocalDateTime.now());
        accounts.setCreatedBy(customer.getEmail());
        return accounts;
    }
}
