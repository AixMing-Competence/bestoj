package com.aixming.bestojbackendserviceclient.service;


import com.aixming.bestojbackendmodel.model.entity.Question;
import com.aixming.bestojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 题目服务
 *
 * @author AixMing
 * @since 2025-03-19 20:19:52
 */
@FeignClient(name = "bestoj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/question/get")
    Question getQuestionById(@RequestParam("id") long id);

    @GetMapping("/question_submit/get")
    QuestionSubmit getQuestionSubmitById(@RequestParam("id") long id);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

}
