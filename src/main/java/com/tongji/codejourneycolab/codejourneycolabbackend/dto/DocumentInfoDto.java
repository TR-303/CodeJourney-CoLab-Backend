package com.tongji.codejourneycolab.codejourneycolabbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

// 请求文章列表时的返回类型，不包含文档的内容。
@Data
public class DocumentInfoDto {
    private Integer id;
    private Integer ownerId;
    private LocalDateTime createTime;
    private LocalDateTime lastModifiedTime;
    private String title;
}
