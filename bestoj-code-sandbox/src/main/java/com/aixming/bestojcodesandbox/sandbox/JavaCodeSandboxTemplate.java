package com.aixming.bestojcodesandbox.sandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.aixming.bestojcodesandbox.model.ExecuteCodeRequest;
import com.aixming.bestojcodesandbox.model.ExecuteCodeResponse;
import com.aixming.bestojcodesandbox.model.ExecuteMessage;
import com.aixming.bestojcodesandbox.model.JudgeInfo;
import com.aixming.bestojcodesandbox.utils.ProcessUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Java 代码沙箱模板方法实现
 */
@Slf4j
public class JavaCodeSandboxTemplate implements CodeSandbox {

    private final String GLOBAL_CODE_DIR = ".temp";

    private final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        // 将用户代码复制到本地
        File userCodeFile = saveCodeToFile(code);
        // 编译代码
        ExecuteMessage compileFileExecuteMessage = compileCode(userCodeFile);
        System.out.println(compileFileExecuteMessage);
        // 运行代码，获取输出结果列表
        List<ExecuteMessage> executeMessageList = runCode(userCodeFile, inputList);
        // 收集处理输出结果列表，封装执行代码响应
        ExecuteCodeResponse executeCodeResponse = collectResultMessage(executeMessageList);
        // 删除本地用户代码
        boolean del = deleteUserCode(userCodeFile.getParentFile());
        if (!del) {
            log.error("delete userCodeFile error, userCodeFilePath = {}", userCodeFile.getAbsolutePath());
        }
        return executeCodeResponse;
    }

    /**
     * 1. 把用户提交的代码保存为文件
     *
     * @param code
     * @return
     */
    public File saveCodeToFile(String code) {
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
        return FileUtil.writeUtf8String(code, userCodePath);
    }

    /**
     * 2. 编译用户代码
     *
     * @param userCodeFile
     * @return
     */
    public ExecuteMessage compileCode(File userCodeFile) {
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());
        try {
            Process process = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage executeMessage = ProcessUtil.runProcessAndGetMessage(process, "编译");
            if (executeMessage.getExitCode() != 0) {
                throw new RuntimeException("编译错误");
            }
            return executeMessage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 3. 运行代码，获取输出结果列表
     *
     * @param userCodeFile
     * @param inputList
     * @return
     */
    public List<ExecuteMessage> runCode(File userCodeFile, List<String> inputList) {
        // 3. 运行代码，得到结果
        ArrayList<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String input : inputList) {
            String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main", userCodeFile.getParent());
            try {
                Process process = Runtime.getRuntime().exec(runCmd);
                // ExecuteMessage executeMessage = ProcessUtil.runProcessAndGetMessage(process, input);
                ExecuteMessage executeMessage = ProcessUtil.runInteractProcessAndGetMessage(process, input);
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (IOException e) {
//                return getErrorResponse(e);
                throw new RuntimeException("运行代码出错", e);
            }
        }
        return executeMessageList;
    }


    /**
     * 4. 收集整理输出结果
     *
     * @param executeMessageList
     * @return
     */
    public ExecuteCodeResponse collectResultMessage(List<ExecuteMessage> executeMessageList) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        // 取最大值判断是否超时和内存溢出
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
        // 本地运行 java 代码获取内存使用情况需要使用第三方库，暂未实现
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }

    /**
     * 5. 删除用户代码
     *
     * @param userCodeParentFile
     */
    public boolean deleteUserCode(File userCodeParentFile) {
        if (userCodeParentFile.exists()) {
            boolean del = FileUtil.del(userCodeParentFile);
            System.out.println("删除" + (del ? "成功" : "失败"));
            return del;
        }
        return true;
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
