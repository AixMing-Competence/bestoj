package com.aixming.bestojcodesandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author AixMing
 * @since 2025-03-20 11:49:58
 */
@RestController
public class MainController {
    @GetMapping("/health")
    public String headlth() {
        return "ok";
    }
}
