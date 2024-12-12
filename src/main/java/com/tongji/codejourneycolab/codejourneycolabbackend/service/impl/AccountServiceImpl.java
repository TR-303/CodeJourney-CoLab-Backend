package com.tongji.codejourneycolab.codejourneycolabbackend.service.impl;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.UserInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.User;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.InvalidCredentialsException;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.UsernameAlreadyExistsException;
import com.tongji.codejourneycolab.codejourneycolabbackend.mapper.UserMapper;
import com.tongji.codejourneycolab.codejourneycolabbackend.security.JwtTokenProvider;
import com.tongji.codejourneycolab.codejourneycolabbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) throws InvalidCredentialsException {
        User user = userMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("User not found or invalid password");
        }
        return jwtTokenProvider.generateToken(user.getId());
    }

    @Override
    public void register(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userMapper.insert(user);
    }

    @Override
    public UserInfoDto getUserInfoById(Integer id) throws InvalidCredentialsException {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new InvalidCredentialsException("User not found");
        }
        return new UserInfoDto(user.getUsername(), user.getEmail());
    }

    @Override
    public void editUserInfoById(Integer id, UserInfoDto userInfoDto) throws InvalidCredentialsException {
        User user = userMapper.selectByUsername(userInfoDto.getUsername());
        if (user != null && !Objects.equals(user.getId(), id)) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        user = userMapper.selectById(id);
        if (user == null) {
            throw new InvalidCredentialsException("User not found");
        }

        user.setUsername(userInfoDto.getUsername());
        user.setEmail(userInfoDto.getEmail());
        userMapper.updateById(user);
    }
}