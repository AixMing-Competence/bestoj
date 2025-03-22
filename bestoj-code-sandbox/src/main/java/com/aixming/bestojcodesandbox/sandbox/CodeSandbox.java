package com.aixming.bestojcodesandbox.sandbox;

import com.aixming.bestojcodesandbox.model.ExecuteCodeRequest;
import com.aixming.bestojcodesandbox.model.ExecuteCodeResponse;

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
