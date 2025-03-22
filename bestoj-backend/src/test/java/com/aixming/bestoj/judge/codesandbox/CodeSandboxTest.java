package com.aixming.bestoj.judge.codesandbox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author AixMing
 * @since 2025-03-19 19:39:11
 */
@SpringBootTest
class CodeSandboxTest {

    @Value("${codesandbox.type}")
    private String type;

    @Test
    void executeCode() throws ClassNotFoundException {
        
    }

}