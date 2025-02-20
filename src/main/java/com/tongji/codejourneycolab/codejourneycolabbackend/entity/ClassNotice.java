package com.tongji.codejourneycolab.codejourneycolabbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("class_notice") // 请替换为实际表名
public class ClassNotice {
    // 对应 class_id 字段
    @TableField("class_id")
    private Integer classId;

    // 对应 id 字段，为主键且自增
    @TableId(type = IdType.AUTO)
    private Integer id;

    // 对应 title 字段
    @TableField("title")
    private String title;

    // 对应 content 字段
    @TableField("content")
    private String content;

    // 对应 create_time 字段
    @TableField("create_time")
    private Date createTime;

    // 新增 teacherName 字段（你可能需要在返回数据时填充该字段）
    @TableField(exist = false) // 不直接映射到数据库
    private String teacherName;

    public ClassNotice(Integer classId,String title, String content){
        this.classId = classId;
        this.title = title;
        this.content = content;
        this.createTime = new Date();
    }
}
