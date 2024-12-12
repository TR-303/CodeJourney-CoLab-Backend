package com.tongji.codejourneycolab.codejourneycolabbackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SubmissionDetail extends Submission {
    private String code;
    private String firstFailureOutput;
}
