package com.aixming.bestojbackendjudgeservice.judge.strategy;

import com.aixming.bestojbackendmodel.model.codesandbox.JudgeInfo;
import com.aixming.bestojbackendmodel.model.entity.JudgeCase;
import com.aixming.bestojbackendmodel.model.entity.Question;
import com.aixming.bestojbackendmodel.model.entity.QuestionSubmit;
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
