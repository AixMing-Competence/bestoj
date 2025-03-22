package com.aixming.bestoj.judge.codesandbox.impl;

import com.aixming.bestoj.judge.codesandbox.CodeSandbox;
import com.aixming.bestoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.aixming.bestoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱
 *
 * @author AixMing
 * @since 2025-03-19 19:48:31
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
