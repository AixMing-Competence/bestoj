package com.aixming.bestojcodesandbox.security;

import java.security.Permission;

/**
 * 禁用所有权限安全管理器
 *
 * @author AixMing
 * @since 2025-03-20 19:57:26
 */
public class DenySecurityManager extends SecurityManager {
    /**
     * 检查所有权限
     *
     * @param perm the requested permission.
     */
    @Override
    public void checkPermission(Permission perm) {
        throw new SecurityException("权限异常: " + perm.toString());
    }
}
