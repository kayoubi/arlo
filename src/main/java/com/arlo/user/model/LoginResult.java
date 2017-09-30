package com.arlo.user.model;

public class LoginResult {
    private final boolean success;

    public LoginResult(final boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
