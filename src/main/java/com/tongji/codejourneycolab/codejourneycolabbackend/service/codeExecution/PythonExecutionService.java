package com.tongji.codejourneycolab.codejourneycolabbackend.service.codeExecution;

import com.tongji.codejourneycolab.codejourneycolabbackend.entity.SubmissionDetail;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.TestCase;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PythonExecutionService {

    public SubmissionDetail executePythonCode(String code, List<TestCase> testCases) {
        int attemptNum = 1;  // 任意整数
        LocalDateTime submitTime = LocalDateTime.now();
        String language = "Python";
        int state = 1;  // 初始设为“通过”
        int passCount = 0;
        double maxTime = 0; // 记录最大执行时间
        String firstFailureOutput = null;

        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            // 打印传入的代码
            System.out.println("Code: " + code);

            // 执行代码，支持Python 2.x 语法
            interpreter.exec(code);  // 直接执行传入的代码

            // 遍历所有测试用例并执行
            for (TestCase testCase : testCases) {
                System.out.println("testCase:" + testCase);
                String input = testCase.getInput();
                String expectedOutput = testCase.getOutput();

                // 记录执行开始时间
                long startTime = System.nanoTime();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                interpreter.setOut(ps);

                interpreter.exec(code);

                String actualOutput = baos.toString().trim();  // 获取输出内容
                System.out.println("actualOutput:" + actualOutput);

                // 记录执行结束时间并计算运行时间
                long endTime = System.nanoTime();
                double elapsedTime = (endTime - startTime) / 1000000.0; // 转换为毫秒

                if (!actualOutput.equals(expectedOutput)) {
                    state = 3;  // 结果错误
                    firstFailureOutput = actualOutput;  // 记录第一个失败的输出
                    break;
                } else {
                    passCount++;
                }

                maxTime = Math.max(maxTime, elapsedTime);  // 保留最大执行时间
            }
        } catch (Exception e) {
            state = 2;  // 编译错误
            System.out.println("Compilation Error: " + e.getMessage());
        }

        // 构造并返回SubmissionDetail对象
        SubmissionDetail submissionDetail = new SubmissionDetail();
        submissionDetail.setAttemptNum(attemptNum);
        submissionDetail.setSubmitTime(submitTime);
        submissionDetail.setLanguage(language);
        submissionDetail.setState(state);
        submissionDetail.setPassCount(passCount);
        submissionDetail.setTotalTime(maxTime);
        submissionDetail.setCode(code);
        submissionDetail.setFirstFailureOutput(firstFailureOutput);

        return submissionDetail;
    }
}


