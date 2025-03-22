package com.aixming.bestojcodesandbox.security;

import cn.hutool.core.io.FileUtil;

/**
 * @author AixMing
 * @since 2025-03-20 20:25:05
 */
public class TestSecurityManager {
    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());
        FileUtil.writeUtf8String("hello","aaa.txt");
    }
}
