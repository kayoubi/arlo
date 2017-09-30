package com.arlo.user.service;

import com.arlo.user.dao.AccountRepository;
import com.arlo.user.domain.Account;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author khaled
 */
@Service
public class AccountService implements UserDetailsService {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(Account account) {
        if (accountRepository.findByUsername(account.username) != null) {
            throw new RuntimeException("user already exist");
        }
        account.password = encoder.encode(account.password);
        return accountRepository.save(account);
    }

    public boolean login(String username, String password) {
        Account a = accountRepository.findByUsername(username);
        return a != null && encoder.matches(password, a.password);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }


    public void deleteAll() {
        accountRepository.deleteAll();
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account != null) {
            return new User(username, account.password, Collections.emptyList());
        }
        throw new UsernameNotFoundException(username);
    }
}
