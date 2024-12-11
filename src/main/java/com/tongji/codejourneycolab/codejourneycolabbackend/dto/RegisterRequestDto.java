package com.tongji.codejourneycolab.codejourneycolabbackend.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String username;
    private String password;
    // 未来可能还有更多
}
