package com.tongji.codejourneycolab.codejourneycolabbackend.service.codeExecution;

import com.tongji.codejourneycolab.codejourneycolabbackend.entity.SubmissionDetail;
import com.tongji.codejourneycolab.codejourneycolabbackend.entity.TestCase;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PythonExecutionService {

    public SubmissionDetail executePythonCode(String code, List<TestCase> testCases) {
        System.out.println("code:" + code);
        int attemptNum = 1;  // 任意整数
        LocalDateTime submitTime = LocalDateTime.now();
        String language = "Python";
        int state = 1;  // 初始设为“通过”
        int passCount = 0;
        double maxTime = 0; // 记录最大执行时间
        String firstFailureOutput = null;

        // python程序的绝对路径，在windows中用"\\"分隔，在Linux中用"/"分隔
        String pyPath = "E:\\python39\\python.exe";  // Python解释器路径

        // 临时文件路径，用来存储代码
        String codeFilePath = "E:\\temp_code.py";

        // 将代码写入到一个临时Python文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(codeFilePath))) {
            writer.write(code);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            state = 2;  // 编译错误
        }

        // 遍历所有测试用例并执行
        for (TestCase testCase : testCases) {
            String input = testCase.getInput();
            String expectedOutput = testCase.getOutput();

            try {
                // 设置运行Python程序的命令，传入Python程序路径和代码文件路径
                String[] args = new String[]{pyPath, codeFilePath, input};  // 将输入参数作为命令行参数传递

                // 记录执行开始时间
                long startTime = System.nanoTime();

                // 执行Python程序
                ProcessBuilder processBuilder = new ProcessBuilder(args);
                processBuilder.redirectErrorStream(true); // 合并标准输出和错误输出
                Process process = processBuilder.start();

                // 获取Python输出字符串
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder outputBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
                in.close();

                // 等待进程结束
                process.waitFor();

                // 获取程序输出
                String actualOutput = outputBuilder.toString().trim();
                System.out.println("Captured output: " + actualOutput);


                // 比较输出是否正确
                if (!actualOutput.equals(expectedOutput)) {
                    state = 3;  // 结果错误
                    if (firstFailureOutput == null) {
                        firstFailureOutput = actualOutput;  // 记录第一个失败的输出
                    }
                } else {
                    passCount++;
                }

                // 记录执行结束时间并计算运行时间
                long endTime = System.nanoTime();
                double elapsedTime = (endTime - startTime) / 1000000.0; // 转换为毫秒

                maxTime = Math.max(maxTime, elapsedTime);  // 更新最大执行时间

            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
                state = 2;  // 编译错误
            }
        }

        return new SubmissionDetail(
                attemptNum, submitTime, language, state, passCount, maxTime, code, firstFailureOutput);
    }
}


