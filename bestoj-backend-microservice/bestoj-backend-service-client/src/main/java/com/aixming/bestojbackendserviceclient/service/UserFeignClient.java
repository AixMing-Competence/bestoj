package com.aixming.bestojbackendserviceclient.service;

import com.aixming.bestojbackendcommon.common.common.ErrorCode;
import com.aixming.bestojbackendcommon.common.exception.BusinessException;
import com.aixming.bestojbackendmodel.model.entity.User;
import com.aixming.bestojbackendmodel.model.enums.UserRoleEnum;
import com.aixming.bestojbackendmodel.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

import static com.aixming.bestojbackendcommon.common.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author AixMing
 * @since 2025-03-24 21:51:39
 */
@FeignClient(name = "bestoj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 据 id 获取用户
     *
     * @param id
     * @return
     */
    @GetMapping("/get/id")
    User getById(@RequestParam("id") long id);

    /**
     * 获取当前登录用户
     *
     * @return
     */
    @GetMapping("/get/login")
    default User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(USER_LOGIN_STATE);
        // 判断是否已经登录
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @GetMapping("get/ids")
    List<User> listByIds(@RequestParam("ids") Collection<Long> ids);

    default boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

}
