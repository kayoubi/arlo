package com.arlo.user.service;

import com.arlo.user.dao.AccountRepository;
import com.arlo.user.domain.Account;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.List;

/**
 * @author khaled
 */
@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(Account account) {
        if (accountRepository.findByUsername(account.username) != null) {
            throw new RuntimeException("user already exist");
        }
        account.password = encrypt(account.password);
        return accountRepository.save(account);
    }

    public boolean login(String username, String password) {
        Account a = accountRepository.findByUsername(username);
        return a != null && a.password.equals(encrypt(password));
    }

    public List<Account> findAll() {
        Arrays.stream(Security.getProviders()).forEach(System.out::println);
        return accountRepository.findAll();
    }

    private String encrypt(final String password) {
        try {
            return new String(MessageDigest.getInstance("SHA-512").digest(password.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return password;
    }

    public void deleteAll() {
        accountRepository.deleteAll();
    }
}
