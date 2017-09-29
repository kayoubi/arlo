package com.arlo.user.domain;

import org.springframework.data.annotation.Id;

/**
 * @author khaled
 */
public class Account {
    @Id
    public String id;

    public String username;
    public String password;
    public String firstName;
    public String lastName;

    public Account() {}

    public Account(String username, String password, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
