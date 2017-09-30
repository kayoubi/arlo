package com.arlo.user;

import com.arlo.user.domain.Account;
import com.arlo.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountApplication {
    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            accountService.deleteAll();
            accountService.create(new Account("kayoubi", "letme1n", "Khaled", "Ayoubi"));
        };
    }
}
