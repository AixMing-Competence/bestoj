package com.aixming.bestojbackenduserservice.controller.inner;

import com.aixming.bestojbackendmodel.model.entity.User;
import com.aixming.bestojbackendserviceclient.service.UserFeignClient;
import com.aixming.bestojbackenduserservice.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * @author AixMing
 * @since 2025-03-25 09:17:35
 */
@RestController
@RequestMapping("/user/inner")
public class UserInnerController implements UserFeignClient {

    @Resource
    private UserService userService;

    @Override
    public User getById(long id) {
        return userService.getById(id);
    }

    @Override
    @GetMapping("/get/ids")
    public List<User> listByIds(Collection<Long> ids) {
        return userService.listByIds(ids);
    }

}
