package com.tongji.codejourneycolab.codejourneycolabbackend.dto;

import lombok.Data;
@Data
public class ClassInfoDto {
    private Integer id;
    private String className;
    private Integer teacherId;
    private String teacherName;
    private Integer capacity;
}
