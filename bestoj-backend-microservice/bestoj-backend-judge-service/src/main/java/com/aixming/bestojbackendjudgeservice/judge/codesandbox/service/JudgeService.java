package com.aixming.bestojbackendjudgeservice.judge.codesandbox.service;


import com.aixming.bestojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 *
 * @author AixMing
 * @since 2025-03-19 20:19:52
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);

}
