package com.tongji.codejourneycolab.codejourneycolabbackend;

import com.tongji.codejourneycolab.codejourneycolabbackend.entity.User;
import com.tongji.codejourneycolab.codejourneycolabbackend.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CodeJourneyCoLabBackendApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void databaseConnectionTest() {
        User user = userMapper.selectById(1);
        Assertions.assertNotNull(user);
    }

}
