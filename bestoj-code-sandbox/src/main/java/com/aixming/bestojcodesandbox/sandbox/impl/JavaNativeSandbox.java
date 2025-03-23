package com.aixming.bestojcodesandbox.sandbox.impl;

import cn.hutool.core.io.resource.ResourceUtil;
import com.aixming.bestojcodesandbox.model.ExecuteCodeRequest;
import com.aixming.bestojcodesandbox.model.ExecuteCodeResponse;
import com.aixming.bestojcodesandbox.sandbox.JavaCodeSandboxTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Java 本地沙箱
 *
 * @author AixMing
 * @since 2025-03-20 14:56:31
 */
@Service
public class JavaNativeSandbox extends JavaCodeSandboxTemplate {

    private String GLOBAL_CODE_DIR = ".temp";

    private String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static void main(String[] args) {
        JavaNativeSandbox javaNativeSandbox = new JavaNativeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setLanguage("java");
        executeCodeRequest.setInputList(Arrays.asList("11 23", "31 41"));
        String code = ResourceUtil.readUtf8Str("code/Main.java");
        executeCodeRequest.setCode(code);
        ExecuteCodeResponse executeCodeResponse = javaNativeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }

}
