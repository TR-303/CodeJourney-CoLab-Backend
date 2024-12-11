package com.tongji.codejourneycolab.codejourneycolabbackend.controller;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.LoginRequestDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.dto.RegisterRequestDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.InvalidCredentialsException;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.UsernameAlreadyExistsException;
import com.tongji.codejourneycolab.codejourneycolabbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            return ResponseEntity.ok(accountService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto registerRequestDto) {
        try {
            accountService.register(registerRequestDto.getUsername(), registerRequestDto.getPassword());
            return ResponseEntity.ok("Register success");
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }
}
