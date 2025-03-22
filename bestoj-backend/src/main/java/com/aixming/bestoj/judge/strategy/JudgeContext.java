package com.aixming.bestoj.judge.strategy;

import com.aixming.bestoj.model.entity.JudgeCase;
import com.aixming.bestoj.judge.codesandbox.model.JudgeInfo;
import com.aixming.bestoj.model.entity.Question;
import com.aixming.bestoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @author AixMing
 * @since 2025-03-19 21:11:21
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private Question question;

    private List<JudgeCase> judgeCaseList;

    private QuestionSubmit questionSubmit;

}
