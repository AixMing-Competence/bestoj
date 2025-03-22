package com.aixming.bestojcodesandbox.sandbox.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.aixming.bestojcodesandbox.model.ExecuteCodeRequest;
import com.aixming.bestojcodesandbox.model.ExecuteCodeResponse;
import com.aixming.bestojcodesandbox.model.ExecuteMessage;
import com.aixming.bestojcodesandbox.model.JudgeInfo;
import com.aixming.bestojcodesandbox.sandbox.CodeSandbox;
import com.aixming.bestojcodesandbox.utils.ProcessUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Java 本地沙箱
 *
 * @author AixMing
 * @since 2025-03-20 14:56:31
 */
public class JavaNativeSandbox implements CodeSandbox {

    private String GLOBAL_CODE_DIR = ".temp";

    private String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static void main(String[] args) {
        // JavaNativeSandbox javaNativeSandbox = new JavaNativeSandbox();
        // ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        // executeCodeRequest.setLanguage("java");
        // executeCodeRequest.setInputList(Arrays.asList("11 23", "31 41"));
        // String code = ResourceUtil.readUtf8Str("code/Main.java");
        // executeCodeRequest.setCode(code);
        // ExecuteCodeResponse executeCodeResponse = javaNativeSandbox.executeCode(executeCodeRequest);
        // System.out.println(executeCodeResponse);

        String[] strings = {"java", "-cp", "/app"};
        String[] newStrinfs = ArrayUtil.append(strings, "1", "2");
        System.out.println(Arrays.toString(newStrinfs));
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();

        String projectPath = System.getProperty("user.dir");
        // 全局代码文件
        String globalCodePath = projectPath + File.separator + GLOBAL_CODE_DIR;
        if (!FileUtil.exist(globalCodePath)) {
            FileUtil.mkdir(globalCodePath);
        }
        // 隔离用户代码
        String userCodeParentPath = globalCodePath + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        // 将用户的代码写进文件当中
        File userCodeFile = FileUtil.writeUtf8String(code, userCodePath);

        // 2. 编译代码，获得 .class 文件
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodePath);
        try {
            Process process = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtil.runProcessAndGetMessage(process, "编译");
            System.out.println(executeMessage);
        } catch (IOException e) {
            return getErrorResponse(e);
        }
        // 3. 运行代码，得到结果
        ArrayList<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inputList) {
            String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, input);
            try {
                Process process = Runtime.getRuntime().exec(runCmd);
                // ExecuteMessage executeMessage = ProcessUtil.runProcessAndGetMessage(process, input);
                ExecuteMessage executeMessage = ProcessUtil.runInteractProcessAndGetMessage(process, input);
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (IOException e) {
                return getErrorResponse(e);
            }
        }
        // 4. 收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        // 取最大值判断是否超时
        long maxTime = 0;
        ArrayList<String> outputList = new ArrayList<>();
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            // 如果没有出错信息出现，则执行消息中的信息就是程序执行的输出结果
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {
                maxTime = Math.max(time, maxTime);
            }
        }
        // 如果正常执行完成
        if (outputList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }
        executeCodeResponse.setOutputList(outputList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        // 要借助第三方库来获取内存，过于麻烦，可以作为一个扩展点
        executeCodeResponse.setJudgeInfo(judgeInfo);
        // 5. 文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }

    /**
     * 获取错误响应
     *
     * @param e
     * @return
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 表示代码沙箱错误（可能是编译错误）
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }
}
