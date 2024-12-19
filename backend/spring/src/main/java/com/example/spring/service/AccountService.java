package com.example.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.spring.model.Account;
import com.example.spring.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getUserById(String accountid) {
        return accountRepository.findById(accountid).orElse(null);
    }

    public Account getAccountByUserid(String userid) {
        return accountRepository.findByUserid(userid).orElse(null);
    }
}
