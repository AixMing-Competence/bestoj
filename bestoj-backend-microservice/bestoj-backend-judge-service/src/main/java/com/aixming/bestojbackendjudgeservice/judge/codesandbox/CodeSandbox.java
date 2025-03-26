package com.aixming.bestojbackendjudgeservice.judge.codesandbox;

import com.aixming.bestojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.aixming.bestojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱
 *
 * @author AixMing
 * @since 2025-03-19 19:32:11
 */
public interface CodeSandbox {
    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
