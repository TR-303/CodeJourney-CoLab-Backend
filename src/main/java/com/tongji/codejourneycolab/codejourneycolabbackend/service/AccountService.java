package com.tongji.codejourneycolab.codejourneycolabbackend.service;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.UserInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.InvalidCredentialsException;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.UsernameAlreadyExistsException;

public interface AccountService {
    String login(String username, String password) throws UsernameAlreadyExistsException;

    void register(String username, String password, String email);

    UserInfoDto getUserInfoById(Integer id) throws InvalidCredentialsException;

    void editUserInfoById(Integer id, UserInfoDto userInfoDto) throws InvalidCredentialsException;

    void logout(String token);

}
