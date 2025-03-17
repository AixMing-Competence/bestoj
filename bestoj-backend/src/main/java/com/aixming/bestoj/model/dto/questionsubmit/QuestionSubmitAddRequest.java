package com.aixming.bestoj.model.dto.questionsubmit;

import com.aixming.bestoj.model.entity.JudgeInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建题目提交请求
 *
 * @author AixMing
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 对象）
     */
    private JudgeInfo judgeInfo;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;

}
