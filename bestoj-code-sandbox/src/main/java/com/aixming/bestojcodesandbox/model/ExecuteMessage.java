package com.aixming.bestojcodesandbox.model;

import lombok.Data;

/**
 * 进程执行信息
 * @author AixMing
 * @since 2025-03-20 15:50:02
 */
@Data
public class ExecuteMessage {
    
    private Integer exitCode;
    
    private String message;
    
    private String errorMessage;
    
    private Long time;
    
}
