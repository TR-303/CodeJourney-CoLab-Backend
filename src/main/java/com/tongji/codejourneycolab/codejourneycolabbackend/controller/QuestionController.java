package com.tongji.codejourneycolab.codejourneycolabbackend.controller;

import com.tongji.codejourneycolab.codejourneycolabbackend.entity.*;
import com.tongji.codejourneycolab.codejourneycolabbackend.mapper.QuestionMapper;
import com.tongji.codejourneycolab.codejourneycolabbackend.service.codeExecution.PythonExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    QuestionMapper questionMapper;

    //获取题目列表
    @RequestMapping("/getList")
    public List<Question> getQuestionList() {
        List<Question> questionList = questionMapper.getQuestionList();
        System.out.print("questionList:" + questionList);
        return questionList;
    }

    //获取题目详情
    @RequestMapping("/get")
    public Question getQuestion(@RequestParam int question_id) {
        QuestionDetail question = questionMapper.getQuestionById(question_id);
        question.setTests(questionMapper.getTestCaseListByQuestionId(question_id));
//        System.out.println(question);
        return question;
    }

    //获取提交过的题目
    @RequestMapping("/getAttemptedQuestionList")
    public List<QuestionSubmitted> getAttemptedQuestionList(@RequestParam int userId) {
        List<QuestionSubmitted> attemptedList = questionMapper.getAttemptedQuestionList(userId);
        for (QuestionSubmitted attemptedQuestion : attemptedList) {
            attemptedQuestion.setState(questionMapper.getQuestionState(userId, attemptedQuestion.getId()));
        }
//        System.out.print("attemptedList:" + attemptedList);
        return attemptedList;
    }

    //查看某一道题的所有提交
    @RequestMapping("/getsubmissionlist")
    public List<Submission> getSubmissionList(@RequestParam int userId, @RequestParam int questionId) {
        List<Submission> submissionList = questionMapper.getSubmissionList(userId, questionId);
//        System.out.println("submissionList:" + submissionList);
        return submissionList;
    }

    //查看一次提交
    @RequestMapping("/getsubmission")
    public SubmissionDetail getSubmission(@RequestParam int userId, @RequestParam int questionId, @RequestParam int attemptNum) {
        SubmissionDetail submissionDetail = questionMapper.getSubmission(userId, questionId, attemptNum);
//        System.out.println(submissionDetail);
        return submissionDetail;
    }

    //发送代码，编译，运行，比较，返回结果
    @RequestMapping("/run")
    public SubmissionDetail runQuestion(@RequestParam int questionId, @RequestParam String code) {
        List<TestCase> testCase = questionMapper.getTestCaseListByQuestionId(questionId);
//        System.out.println(testCase);
//        System.out.println(code);
        PythonExecutionService pythonExecutionService = new PythonExecutionService();
        SubmissionDetail result = pythonExecutionService.executePythonCode(code, testCase);
//        System.out.println(result);
        return result;
    }
}