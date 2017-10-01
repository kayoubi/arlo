package com.arlo.user.controller;

import com.arlo.user.domain.Account;
import com.arlo.user.model.LoginResult;
import com.arlo.user.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
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
        return accountService.findAll().stream().peek(this::obscure).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public Account get(@PathVariable String id) {
        return obscure(accountService.find(id));
    }

    @PostMapping("/accounts")
    public ResponseEntity create(@RequestBody Account account) {
        final String id = accountService.create(account).id;
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login2")
    public LoginResult login(@RequestBody Account account, HttpServletResponse response) {
        final boolean success = accountService.login(account.username, account.password);
        response.setStatus(success ? HttpStatus.OK.value() : HttpStatus.UNAUTHORIZED.value());
        return new LoginResult(success);
    }

    private Account obscure(Account account) {
        account.password = "*****";
        return account;
    }
}
