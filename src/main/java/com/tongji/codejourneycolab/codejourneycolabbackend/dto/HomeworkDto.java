package com.tongji.codejourneycolab.codejourneycolabbackend.dto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class HomeworkDto {
    private Integer problemId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dueTime;
    private String title;
    private String status;
}
