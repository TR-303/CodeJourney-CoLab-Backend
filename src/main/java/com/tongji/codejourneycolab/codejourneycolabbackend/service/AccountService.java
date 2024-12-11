package com.tongji.codejourneycolab.codejourneycolabbackend.service;

import com.tongji.codejourneycolab.codejourneycolabbackend.exception.UsernameAlreadyExistsException;

public interface AccountService {
    String login(String username, String password) throws UsernameAlreadyExistsException;

    void register(String username, String password);

}
