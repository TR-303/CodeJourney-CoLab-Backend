package com.tongji.codejourneycolab.codejourneycolabbackend.controller;

import com.tongji.codejourneycolab.codejourneycolabbackend.dto.*;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.ClassNotice;
import com.tongji.codejourneycolab.codejourneycolabbackend.service.impl.ClassServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassServiceImpl classServiceImpl;

    // 获取班级列表
    @GetMapping("/getclasslist")
    public ResponseEntity<List<ClassInfoDto>> getClasses(@RequestAttribute Integer id) {
        try {
            List<ClassInfoDto> classList = classServiceImpl.getClassList(id);
            return ResponseEntity.ok(classList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 加入班级
    @PostMapping("/join")
    public ResponseEntity<Boolean> joinClass(@RequestAttribute Integer id,@RequestBody JoinClassCodeDto joinCode) {
        try {
            Boolean result = classServiceImpl.joinClass(joinCode.getClassCode(),id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 创建班级
    @PostMapping("/create")
    public ResponseEntity<Boolean> createClass(@RequestAttribute Integer id,@RequestParam String className) {
        try {
            Boolean result = classServiceImpl.createClass(id,className);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 获取班级成员列表
    @GetMapping("/getstulist")
    public ResponseEntity<List<UserInfoDto>> getStudentsInClass(@RequestParam Integer classId) {
        try {
            List<UserInfoDto> studentList = classServiceImpl.getStuListByClassId(classId);
            return ResponseEntity.ok(studentList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 生成加入班级的代码
    @PostMapping("/generatecode")
    public ResponseEntity<String> generateJoinCode(@RequestParam Integer classId) {
        try {
            String joinCode = classServiceImpl.generateJoinCode(classId);
            return ResponseEntity.ok(joinCode);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 获取通知列表
    @GetMapping("/getNotice")
    public ResponseEntity<List<ClassNotice>> getNotice(@RequestParam Integer classId) {
        try {
            List<ClassNotice> notifications = classServiceImpl.getNotice(classId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 创建通知
    @PostMapping("/createNotice")
    public ResponseEntity<Boolean> createNotice(@RequestAttribute Integer id,
                                                @RequestBody NoticeRequestDto request) {
        try {
            Boolean result = classServiceImpl.createNotice(request.getClassId(), request.getTitle(),request.getContent());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 获取作业列表
    @GetMapping("/getHomeworkList")
    public ResponseEntity<List<HomeworkDto>> getHomeworkList(@RequestAttribute Integer id,
                                                             @RequestParam Integer classId) {
        try {
            List<HomeworkDto> homeworkList = classServiceImpl.getHomeworkList(id,classId);
            return ResponseEntity.ok(homeworkList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    // 创建作业
    @PostMapping("/createAssignment")
    public ResponseEntity<Boolean> createAssignment(@RequestAttribute Integer id,
                                                    @RequestBody AssignmentRequestDto request) {
        try {
            Boolean result = classServiceImpl.createHomework(request.getClassId(),request.getProblemId(),request.getDueTime());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getStuHomework")
    public ResponseEntity<List<StuStatusDto>> getStuHomework(@RequestParam Integer classId,
                                                      @RequestParam Integer problemId) {
        try {
            List<StuStatusDto> stuStatus = classServiceImpl.getStuHomework(classId, problemId);
            return ResponseEntity.ok(stuStatus);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }

    }
}
