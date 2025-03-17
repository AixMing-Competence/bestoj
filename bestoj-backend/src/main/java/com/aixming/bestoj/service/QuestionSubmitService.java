package com.aixming.bestoj.service;

import com.aixming.bestoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.aixming.bestoj.model.entity.QuestionSubmit;
import com.aixming.bestoj.model.vo.QuestionSubmitVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 题目提交服务
 *
 * @author AixMing
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 校验数据
     *
     * @param questionsubmit
     * @param add 对创建的数据进行校验
     */
    void validQuestionSubmit(QuestionSubmit questionsubmit, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionsubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionsubmitQueryRequest);
    
    /**
     * 获取题目提交封装
     *
     * @param questionsubmit
     * @param request
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionsubmit, HttpServletRequest request);

    /**
     * 分页获取题目提交封装
     *
     * @param questionsubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionsubmitPage, HttpServletRequest request);

}
