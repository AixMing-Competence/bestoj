package com.aixming.bestojbackendjudgeservice.controller.inner;

import com.aixming.bestojbackendjudgeservice.judge.codesandbox.service.JudgeService;
import com.aixming.bestojbackendmodel.model.entity.QuestionSubmit;
import com.aixming.bestojbackendserviceclient.service.JudgeFeignClient;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务内部调用
 *
 * @author AixMing
 * @since 2025-03-24 21:53:19
 */
@RestController
@RequestMapping("/api/judge/inner")
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;

    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }

}
