package com.aixming.bestojbackendjudgeservice.judge.codesandbox.impl;


import com.aixming.bestojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.aixming.bestojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.aixming.bestojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 示例代码沙箱
 *
 * @author AixMing
 * @since 2025-03-19 19:48:31
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("示例代码沙箱");
        return null;
    }
}
