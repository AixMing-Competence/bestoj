package com.aixming.bestoj.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aixming.bestoj.annotation.AuthCheck;
import com.aixming.bestoj.common.BaseResponse;
import com.aixming.bestoj.common.DeleteRequest;
import com.aixming.bestoj.common.ErrorCode;
import com.aixming.bestoj.common.ResultUtils;
import com.aixming.bestoj.constant.UserConstant;
import com.aixming.bestoj.exception.BusinessException;
import com.aixming.bestoj.exception.ThrowUtils;
import com.aixming.bestoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.aixming.bestoj.model.dto.questionsubmit.QuestionSubmitEditRequest;
import com.aixming.bestoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.aixming.bestoj.model.dto.questionsubmit.QuestionSubmitUpdateRequest;
import com.aixming.bestoj.model.entity.QuestionSubmit;
import com.aixming.bestoj.model.entity.User;
import com.aixming.bestoj.model.vo.QuestionSubmitVO;
import com.aixming.bestoj.service.QuestionSubmitService;
import com.aixming.bestoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author AixMing
 */
@RestController
@RequestMapping("/questionsubmit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionsubmitService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建题目提交
     *
     * @param questionsubmitAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionsubmitAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionsubmitAddRequest == null, ErrorCode.PARAMS_ERROR);
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionsubmitAddRequest, questionSubmit);
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(questionsubmitAddRequest.getJudgeInfo()));
        // 数据校验
        questionsubmitService.validQuestionSubmit(questionSubmit, true);
        User loginUser = userService.getLoginUser(request);
        questionSubmit.setUserId(loginUser.getId());
        // 写入数据库
        boolean result = questionsubmitService.save(questionSubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newQuestionSubmitId = questionSubmit.getId();
        return ResultUtils.success(newQuestionSubmitId);
    }

    /**
     * 删除题目提交
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteQuestionSubmit(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        QuestionSubmit oldQuestionSubmit = questionsubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldQuestionSubmit.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionsubmitService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新题目提交（仅管理员可用）
     *
     * @param questionsubmitUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateQuestionSubmit(@RequestBody QuestionSubmitUpdateRequest questionsubmitUpdateRequest) {
        if (questionsubmitUpdateRequest == null || questionsubmitUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionsubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionsubmitUpdateRequest, questionsubmit);
        questionsubmit.setJudgeInfo(JSONUtil.toJsonStr(questionsubmitUpdateRequest.getJudgeInfo()));
        // 数据校验
        questionsubmitService.validQuestionSubmit(questionsubmit, false);
        // 判断是否存在
        long id = questionsubmitUpdateRequest.getId();
        QuestionSubmit oldQuestionSubmit = questionsubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = questionsubmitService.updateById(questionsubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取题目提交（封装类）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<QuestionSubmitVO> getQuestionSubmitVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        QuestionSubmit questionsubmit = questionsubmitService.getById(id);
        ThrowUtils.throwIf(questionsubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(questionsubmitService.getQuestionSubmitVO(questionsubmit, request));
    }

    /**
     * 分页获取题目提交列表（仅管理员可用）
     *
     * @param questionsubmitQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<QuestionSubmit>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionsubmitQueryRequest) {
        long current = questionsubmitQueryRequest.getCurrent();
        long size = questionsubmitQueryRequest.getPageSize();
        // 查询数据库
        Page<QuestionSubmit> questionsubmitPage = questionsubmitService.page(new Page<>(current, size),
                questionsubmitService.getQueryWrapper(questionsubmitQueryRequest));
        return ResultUtils.success(questionsubmitPage);
    }

    /**
     * 分页获取题目提交列表（封装类）
     *
     * @param questionsubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitVOByPage(@RequestBody QuestionSubmitQueryRequest questionsubmitQueryRequest,
                                                                           HttpServletRequest request) {
        long current = questionsubmitQueryRequest.getCurrent();
        long size = questionsubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionSubmit> questionsubmitPage = questionsubmitService.page(new Page<>(current, size),
                questionsubmitService.getQueryWrapper(questionsubmitQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionsubmitService.getQuestionSubmitVOPage(questionsubmitPage, request));
    }

    /**
     * 分页获取当前登录用户创建的题目提交列表
     *
     * @param questionsubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<QuestionSubmitVO>> listMyQuestionSubmitVOByPage(@RequestBody QuestionSubmitQueryRequest questionsubmitQueryRequest,
                                                                             HttpServletRequest request) {
        ThrowUtils.throwIf(questionsubmitQueryRequest == null, ErrorCode.PARAMS_ERROR);
        // 补充查询条件，只查询当前登录用户的数据
        User loginUser = userService.getLoginUser(request);
        questionsubmitQueryRequest.setUserId(loginUser.getId());
        long current = questionsubmitQueryRequest.getCurrent();
        long size = questionsubmitQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<QuestionSubmit> questionsubmitPage = questionsubmitService.page(new Page<>(current, size),
                questionsubmitService.getQueryWrapper(questionsubmitQueryRequest));
        // 获取封装类
        return ResultUtils.success(questionsubmitService.getQuestionSubmitVOPage(questionsubmitPage, request));
    }

    /**
     * 编辑题目提交（给用户使用）
     *
     * @param questionsubmitEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editQuestionSubmit(@RequestBody QuestionSubmitEditRequest questionsubmitEditRequest, HttpServletRequest request) {
        if (questionsubmitEditRequest == null || questionsubmitEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionsubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionsubmitEditRequest, questionsubmit);
        questionsubmit.setJudgeInfo(JSONUtil.toJsonStr(questionsubmitEditRequest));
        // 数据校验
        questionsubmitService.validQuestionSubmit(questionsubmit, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = questionsubmitEditRequest.getId();
        QuestionSubmit oldQuestionSubmit = questionsubmitService.getById(id);
        ThrowUtils.throwIf(oldQuestionSubmit == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldQuestionSubmit.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = questionsubmitService.updateById(questionsubmit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
