package com.aixming.bestojcodesandbox.security;

import java.security.Permission;

/**
 * 默认安全管理器
 *
 * @author AixMing
 * @since 2025-03-20 19:57:26
 */
public class DefaultSecurityManager extends SecurityManager {
    @Override
    public void checkPermission(Permission perm) {
        System.out.println("默认不做任何限制");
        System.out.println(perm);
    }
}
