package com.aixming.bestojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aixming.bestojbackendcommon.common.common.ErrorCode;
import com.aixming.bestojbackendcommon.common.constant.CommonConstant;
import com.aixming.bestojbackendcommon.common.exception.ThrowUtils;
import com.aixming.bestojbackendcommon.common.utils.SqlUtils;
import com.aixming.bestojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.aixming.bestojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.aixming.bestojbackendmodel.model.entity.Question;
import com.aixming.bestojbackendmodel.model.entity.QuestionSubmit;
import com.aixming.bestojbackendmodel.model.entity.User;
import com.aixming.bestojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.aixming.bestojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.aixming.bestojbackendmodel.model.vo.QuestionSubmitVO;
import com.aixming.bestojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.aixming.bestojbackendquestionservice.rabbitmq.MyMessageProducer;
import com.aixming.bestojbackendquestionservice.service.QuestionService;
import com.aixming.bestojbackendquestionservice.service.QuestionSubmitService;
import com.aixming.bestojbackendserviceclient.service.UserFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题目提交服务实现
 *
 * @author AixMing
 */
@Service
@Slf4j
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {

    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    private QuestionService questionService;

    @Resource
    private MyMessageProducer myMessageProducer;

    /**
     * 校验数据
     *
     * @param questionsubmit
     * @param add            对创建的数据进行校验
     */
    @Override
    public void validQuestionSubmit(QuestionSubmit questionsubmit, boolean add) {
        ThrowUtils.throwIf(questionsubmit == null, ErrorCode.PARAMS_ERROR);

        // 创建数据时，参数不能为空
        // if (add) {
        //     ThrowUtils.throwIf(StringUtils.isBlank(title), ErrorCode.PARAMS_ERROR);
        // }
        // 修改数据时，有参数则校验
        // if (StringUtils.isNotBlank(title)) {
        //     ThrowUtils.throwIf(title.length() > 80, ErrorCode.PARAMS_ERROR, "标题过长");
        // }
    }

    /**
     * 获取查询条件
     *
     * @param questionsubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionsubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionsubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long id = questionsubmitQueryRequest.getId();
        String language = questionsubmitQueryRequest.getLanguage();
        Integer status = questionsubmitQueryRequest.getStatus();
        Long questionId = questionsubmitQueryRequest.getQuestionId();
        Long userId = questionsubmitQueryRequest.getUserId();
        Long notId = questionsubmitQueryRequest.getNotId();
        String sortField = questionsubmitQueryRequest.getSortField();
        String sortOrder = questionsubmitQueryRequest.getSortOrder();

        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);

        // 排序规则
        queryWrapper.orderBy(
                SqlUtils.validSortField(sortField),
                CommonConstant.SORT_ORDER_ASC.equals(sortOrder),
                sortField
        );
        return queryWrapper;
    }

    /**
     * 获取题目提交封装
     *
     * @param questionSubmit
     * @param request
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, HttpServletRequest request) {
        // 对象转封装类
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 1. 关联查询用户信息
        Long userId = questionSubmit.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userFeignClient.getById(userId);
        }
        questionSubmitVO.setUser(userFeignClient.getUserVO(user));
        // 2. 处理脱敏
        // 只有管理员和本人才能看到用户提交的代码
        User loginUser = userFeignClient.getLoginUser(request);
        if (!loginUser.getId().equals(userId) && !userFeignClient.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    /**
     * 分页获取题目提交封装
     *
     * @param questionsubmitPage
     * @param request
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionsubmitPage, HttpServletRequest request) {
        List<QuestionSubmit> questionsubmitList = questionsubmitPage.getRecords();
        Page<QuestionSubmitVO> questionsubmitVOPage = new Page<>(questionsubmitPage.getCurrent(), questionsubmitPage.getSize(), questionsubmitPage.getTotal());
        if (CollUtil.isEmpty(questionsubmitList)) {
            return questionsubmitVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionSubmitVO> questionsubmitVOList = questionsubmitList.stream().map(questionsubmit -> {
            return QuestionSubmitVO.objToVo(questionsubmit);
        }).collect(Collectors.toList());

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionsubmitList.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionsubmitVOList.forEach(questionsubmitVO -> {
            Long userId = questionsubmitVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionsubmitVO.setUser(userFeignClient.getUserVO(user));
        });
        // endregion

        questionsubmitVOPage.setRecords(questionsubmitVOList);
        return questionsubmitVOPage;
    }

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 参数校验
        String language = questionSubmitAddRequest.getLanguage();
        Long questionId = questionSubmitAddRequest.getQuestionId();

        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        ThrowUtils.throwIf(languageEnum == null, ErrorCode.PARAMS_ERROR, "编程语言错误");
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, ErrorCode.NOT_FOUND_ERROR, "不存在题目");

        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitAddRequest, questionSubmit);
        questionSubmit.setJudgeInfo("{}");
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setUserId(loginUser.getId());
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setQuestionId(questionId);

        boolean result = save(questionSubmit);
        ThrowUtils.throwIf(!result, ErrorCode.SYSTEM_ERROR, "提交失败");

        // 执行判题服务
        Long questionSubmitId = questionSubmit.getId();
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));
//        CompletableFuture.runAsync(() -> judgeFeignClient.doJudge(questionSubmitId));

        return questionSubmitId;
    }

}
