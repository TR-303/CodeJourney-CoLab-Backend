package com.tongji.codejourneycolab.codejourneycolabbackend.controller;

import com.tongji.codejourneycolab.codejourneycolabbackend.entity.*;
import com.tongji.codejourneycolab.codejourneycolabbackend.mapper.QuestionMapper;
import com.tongji.codejourneycolab.codejourneycolabbackend.service.codeExecution.PythonExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    QuestionMapper questionMapper;

    //获取题目列表
    @GetMapping("/getList")
    public List<Question> getQuestionList() {
        List<Question> questionList = questionMapper.getQuestionList();
        System.out.print("questionList:" + questionList);
        return questionList;
    }

    //获取题目详情
    @GetMapping("/get")
    public Question getQuestion(@RequestParam int questionId) {
        QuestionDetail question = questionMapper.getQuestionById(questionId);
        question.setTests(questionMapper.getTestCaseListByQuestionId(questionId));
//        System.out.println(question);
        return question;
    }

    //获取提交过的题目
    @GetMapping("/getAttemptedQuestionList")
    public List<QuestionSubmitted> getAttemptedQuestionList(@RequestAttribute Integer id) {
        List<QuestionSubmitted> attemptedList = questionMapper.getAttemptedQuestionList(1);
        for (QuestionSubmitted attemptedQuestion : attemptedList) {
            attemptedQuestion.setState(questionMapper.getQuestionState(1, attemptedQuestion.getId()));
        }
//        System.out.print("attemptedList:" + attemptedList);
        return attemptedList;
    }

    //查看某一道题的所有提交
    @GetMapping("/getSubmissionList")
    public List<Submission> getSubmissionList(@RequestAttribute Integer id, @RequestParam int questionId) {
        List<Submission> submissionList = questionMapper.getSubmissionList(id, questionId);
//        System.out.println("submissionList:" + submissionList);
        return submissionList;
    }

    //查看一次提交
    @GetMapping("/getSubmission")
    public SubmissionDetail getSubmission(@RequestAttribute Integer id, @RequestParam int questionId, @RequestParam int attemptNum) {
        SubmissionDetail submissionDetail = questionMapper.getSubmission(id, questionId, attemptNum);
//        System.out.println(submissionDetail);
        return submissionDetail;
    }

    //发送代码，编译，运行，比较，返回结果
    //参数放在Body中进行请求
    @PostMapping("/run")
    public SubmissionDetail runQuestion(@RequestAttribute Integer id, @RequestParam int questionId, @RequestParam String code) {
        List<TestCase> testCase = questionMapper.getTestCaseListByQuestionId(questionId);
//        System.out.println(testCase);
//        System.out.println(code);
        PythonExecutionService pythonExecutionService = new PythonExecutionService();
        SubmissionDetail result = pythonExecutionService.executePythonCode(code, testCase);
//        System.out.println(result);
        result.setAttemptNum(questionMapper.getSubmissionCount(id, questionId) + 1);
        result.setUserId(id);
        result.setQuestionId(questionId);
        questionMapper.addSubmission(result);
        return result;
    }
}
