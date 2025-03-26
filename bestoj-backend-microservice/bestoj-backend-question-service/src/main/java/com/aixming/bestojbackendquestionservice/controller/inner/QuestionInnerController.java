package com.aixming.bestojbackendquestionservice.controller.inner;

import com.aixming.bestojbackendmodel.model.entity.Question;
import com.aixming.bestojbackendmodel.model.entity.QuestionSubmit;
import com.aixming.bestojbackendquestionservice.service.QuestionService;
import com.aixming.bestojbackendquestionservice.service.QuestionSubmitService;
import com.aixming.bestojbackendserviceclient.service.QuestionFeignClient;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 仅服务内部调用
 *
 * @author AixMing
 * @since 2025-03-25 16:00:26
 */
@RestController
@RequestMapping("/question/inner")
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/question/get")
    public Question getQuestionById(@RequestParam long id) {
        return questionService.getById(id);
    }

    @Override
    @GetMapping("/question_submit/get")
    public QuestionSubmit getQuestionSubmitById(@RequestParam long id) {
        return questionSubmitService.getById(id);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

}
