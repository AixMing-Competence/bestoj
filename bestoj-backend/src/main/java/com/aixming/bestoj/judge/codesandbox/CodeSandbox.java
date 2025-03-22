package com.aixming.bestoj.judge.codesandbox;

import com.aixming.bestoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.aixming.bestoj.judge.codesandbox.model.ExecuteCodeResponse;

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
