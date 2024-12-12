package com.tongji.codejourneycolab.codejourneycolabbackend.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Submission {
    private int attemptNum;
    private LocalDateTime submitTime;
    private String language;
    private int state;
    private int passCount;
    private double totalTime;
}
