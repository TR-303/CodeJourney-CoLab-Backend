package com.tongji.codejourneycolab.codejourneycolabbackend.service.impl;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.UserInfoDto;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.User;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.InvalidCredentialsException;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.InvalidRegisterException;
import com.tongji.codejourneycolab.codejourneycolabbackend.exception.IdentityAlreadyExistsException;
import com.tongji.codejourneycolab.codejourneycolabbackend.mapper.UserMapper;
import com.tongji.codejourneycolab.codejourneycolabbackend.security.InvalidTokenManager;
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

    @Autowired
    private InvalidTokenManager invalidTokenManager;

    @Override
    public String login(String identity, String password) throws InvalidCredentialsException {
        if (identity == null || password == null) {
            throw new InvalidCredentialsException("User not found or invalid password");
        }

        User user;
        if (identity.contains("@")) { //邮箱登录
            user = userMapper.selectByEmail(identity);
        } else {    //用户名登录
            user = userMapper.selectByUsername(identity);
        }
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("User not found or invalid password");
        }

        return jwtTokenProvider.generateToken(user.getId());
    }

    @Override
    public void register(String username, String password, String email) throws IdentityAlreadyExistsException, InvalidRegisterException {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            throw new IdentityAlreadyExistsException("Username already exists");
        }
        user = userMapper.selectByEmail(email);
        if (user != null) {
            throw new IdentityAlreadyExistsException("Email already exists");
        }

        // 检查注册合法性
        if (username == null || username.length() < 4 || username.length() > 20 || !username.matches("\\w+")) {
            throw new InvalidRegisterException("Username must be between 4 and 20 characters and contain only alphanumeric characters and underscores");
        }
        if (password == null || password.length() < 8) {
            throw new InvalidRegisterException("Password must be at least 8 characters");
        }
        if (email == null || !email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
            throw new InvalidRegisterException("Must be a valid email address");
        }

        user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        // *注册的默认身份是普通用户
        user.setRole("USER");
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
            throw new IdentityAlreadyExistsException("Username already exists");
        }

        user = userMapper.selectById(id);
        if (user == null) {
            throw new InvalidCredentialsException("User not found");
        }

        user.setUsername(userInfoDto.getUsername());
        user.setEmail(userInfoDto.getEmail());
        userMapper.updateById(user);
    }

    @Override
    public void logout(String token) {
        invalidTokenManager.addInvalidToken(token);
    }
}