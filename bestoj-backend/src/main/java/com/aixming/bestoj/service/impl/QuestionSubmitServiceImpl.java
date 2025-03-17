package com.aixming.bestoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aixming.bestoj.common.ErrorCode;
import com.aixming.bestoj.exception.ThrowUtils;
import com.aixming.bestoj.mapper.QuestionSubmitMapper;
import com.aixming.bestoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.aixming.bestoj.model.entity.JudgeInfo;
import com.aixming.bestoj.model.entity.QuestionSubmit;
import com.aixming.bestoj.model.entity.User;
import com.aixming.bestoj.model.vo.QuestionSubmitVO;
import com.aixming.bestoj.model.vo.UserVO;
import com.aixming.bestoj.service.QuestionSubmitService;
import com.aixming.bestoj.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 题目提交服务实现
 *
 * @author AixMing
 */
@Service
@Slf4j
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper,QuestionSubmit> implements QuestionSubmitService {

    @Resource
    private UserService userService;

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
        String code = questionsubmitQueryRequest.getCode();
        JudgeInfo judgeInfo = questionsubmitQueryRequest.getJudgeInfo();
        Integer status = questionsubmitQueryRequest.getStatus();
        Long questionId = questionsubmitQueryRequest.getQuestionId();
        Long userId = questionsubmitQueryRequest.getUserId();
        Date createTime = questionsubmitQueryRequest.getCreateTime();
        Long notId = questionsubmitQueryRequest.getNotId();
        String searchText = questionsubmitQueryRequest.getSearchText();
        int current = questionsubmitQueryRequest.getCurrent();
        int pageSize = questionsubmitQueryRequest.getPageSize();
        String sortField = questionsubmitQueryRequest.getSortField();
        String sortOrder = questionsubmitQueryRequest.getSortOrder();

        // // 从多字段中搜索
        // if (StringUtils.isNotBlank(searchText)) {
        //     // 需要拼接查询条件
        //     queryWrapper.and(qw -> qw.like("language", searchText));
        // }
        // // 模糊查询
        // queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        // queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        // // JSON 数组查询
        // if (CollUtil.isNotEmpty(tagList)) {
        //     for (String tag : tagList) {
        //         queryWrapper.like("tags", "\"" + tag + "\"");
        //     }
        // }
        // // 精确查询
        // queryWrapper.ne(ObjectUtils.isNotEmpty(notId), "id", notId);
        // queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        // queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // // 排序规则
        // queryWrapper.orderBy(SqlUtils.validSortField(sortField),
        //         sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
        //         sortField);
        return queryWrapper;
    }

    /**
     * 获取题目提交封装
     *
     * @param questionsubmit
     * @param request
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionsubmit, HttpServletRequest request) {
        // 对象转封装类
        QuestionSubmitVO questionsubmitVO = QuestionSubmitVO.objToVo(questionsubmit);

        // todo 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = questionsubmit.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionsubmitVO.setUser(userVO);
        // 2. 已登录，获取用户点赞、收藏状态
        long questionsubmitId = questionsubmit.getId();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            // // 获取点赞
            // QueryWrapper<QuestionSubmitThumb> questionsubmitThumbQueryWrapper = new QueryWrapper<>();
            // questionsubmitThumbQueryWrapper.in("questionsubmitId", questionsubmitId);
            // questionsubmitThumbQueryWrapper.eq("userId", loginUser.getId());
            // QuestionSubmitThumb questionsubmitThumb = questionsubmitThumbMapper.selectOne(questionsubmitThumbQueryWrapper);
            // questionsubmitVO.setHasThumb(questionsubmitThumb != null);
            // // 获取收藏
            // QueryWrapper<QuestionSubmitFavour> questionsubmitFavourQueryWrapper = new QueryWrapper<>();
            // questionsubmitFavourQueryWrapper.in("questionsubmitId", questionsubmitId);
            // questionsubmitFavourQueryWrapper.eq("userId", loginUser.getId());
            // QuestionSubmitFavour questionsubmitFavour = questionsubmitFavourMapper.selectOne(questionsubmitFavourQueryWrapper);
            // questionsubmitVO.setHasFavour(questionsubmitFavour != null);
        }
        // endregion

        return questionsubmitVO;
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
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> questionsubmitIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> questionsubmitIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> questionsubmitIdSet = questionsubmitList.stream().map(QuestionSubmit::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
        }
        // 填充信息
        questionsubmitVOList.forEach(questionsubmitVO -> {
            Long userId = questionsubmitVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionsubmitVO.setUser(userService.getUserVO(user));
        });
        // endregion

        questionsubmitVOPage.setRecords(questionsubmitVOList);
        return questionsubmitVOPage;
    }

}
