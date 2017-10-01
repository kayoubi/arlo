package com.arlo.user.controller;

import com.arlo.user.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author khaled
 */
@ControllerAdvice
public class GlobalController {
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public String badRequest(IllegalArgumentException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({AccountService.EntityNotFoundException.class})
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public void notFound() {
    }
}
