package com.aixming.bestoj.judge.codesandbox.service.impl;

import cn.hutool.json.JSONUtil;
import com.aixming.bestoj.common.ErrorCode;
import com.aixming.bestoj.exception.BusinessException;
import com.aixming.bestoj.exception.ThrowUtils;
import com.aixming.bestoj.judge.codesandbox.CodeSandbox;
import com.aixming.bestoj.judge.codesandbox.CodeSandboxFactory;
import com.aixming.bestoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.aixming.bestoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.aixming.bestoj.judge.codesandbox.service.JudgeService;
import com.aixming.bestoj.judge.strategy.JudgeContext;
import com.aixming.bestoj.judge.strategy.JudgeManager;
import com.aixming.bestoj.model.entity.JudgeCase;
import com.aixming.bestoj.judge.codesandbox.model.JudgeInfo;
import com.aixming.bestoj.model.entity.Question;
import com.aixming.bestoj.model.entity.QuestionSubmit;
import com.aixming.bestoj.model.enums.QuestionSubmitStatusEnum;
import com.aixming.bestoj.service.QuestionService;
import com.aixming.bestoj.service.QuestionSubmitService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AixMing
 * @since 2025-03-19 20:21:39
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private QuestionService questionService;

    @Value("${codesandbox.type}")
    private String codeSandboxType;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 获取题目提交
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        ThrowUtils.throwIf(questionSubmit == null, ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        // 如果不为等待状态，则抛出异常
        Integer status = questionSubmit.getStatus();
        QuestionSubmitStatusEnum questionSubmitStatusEnum = QuestionSubmitStatusEnum.getEnumByValue(status);
        if (!QuestionSubmitStatusEnum.WAITING.equals(questionSubmitStatusEnum)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 修改状态为“判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setUserId(questionId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.JUDGING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        ThrowUtils.throwIf(!update, ErrorCode.SYSTEM_ERROR, "题目提交状态更新失败");

        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取测试用例
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        // 调用代码沙箱，获取执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(codeSandboxType);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .language(language)
                .code(code)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);

        // 判题
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setQuestion(question);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        // 更新数据库中题目提交信息
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        update = questionSubmitService.updateById(questionSubmitUpdate);
        ThrowUtils.throwIf(!update, ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionSubmitId);
        return questionSubmitResult;
    }
}
