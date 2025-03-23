package com.aixming.bestojcodesandbox.controller;

import com.aixming.bestojcodesandbox.model.ExecuteCodeRequest;
import com.aixming.bestojcodesandbox.model.ExecuteCodeResponse;
import com.aixming.bestojcodesandbox.sandbox.impl.JavaDockerCodeSandbox;
import com.aixming.bestojcodesandbox.sandbox.impl.JavaNativeSandbox;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AixMing
 * @since 2025-03-20 11:49:58
 */
@RestController
public class CodeSandboxController {

    /**
     * 定义鉴权请求头和秘钥
     */
    private static final String AUTH_REQUEST_HEAD = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Resource
    private JavaNativeSandbox javaNativeSandbox;

    @Resource
    private JavaDockerCodeSandbox javaDockerCodeSandbox;

    @GetMapping("/health")
    public String headlth() {
        return "ok";
    }

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/executeCode")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        // 基本认证
        String authHeader = request.getHeader(AUTH_REQUEST_HEAD);
        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        return javaDockerCodeSandbox.executeCode(executeCodeRequest);
    }
}
