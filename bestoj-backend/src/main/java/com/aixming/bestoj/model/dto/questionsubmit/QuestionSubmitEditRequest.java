package com.aixming.bestoj.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑题目提交请求
 *
 * @author AixMing
 */
@Data
public class QuestionSubmitEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    private static final long serialVersionUID = 1L;

}
