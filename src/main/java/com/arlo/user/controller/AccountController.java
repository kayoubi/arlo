package com.arlo.user.controller;

import com.arlo.user.domain.Account;
import com.arlo.user.model.LoginResult;
import com.arlo.user.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author khaled
 */
@RestController
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public List<Account> getALL() {
        return accountService.findAll().stream().peek(a -> a.password = "******").collect(Collectors.toList());
    }

    @PostMapping("/accounts")
    public String create(@RequestBody Account account) {
        return accountService.create(account).id;
    }

    @PostMapping("/login2")
    public LoginResult login(@RequestBody Account account) {
        return new LoginResult(accountService.login(account.username, account.password));
    }
}
