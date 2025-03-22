package com.aixming.bestojcodesandbox.security;

import java.security.Permission;

/**
 * 默认安全管理器
 *
 * @author AixMing
 * @since 2025-03-20 19:57:26
 */
public class MySecurityManager extends SecurityManager {
    /**
     * 检查所有权限
     *
     * @param perm the requested permission.
     */
    @Override
    public void checkPermission(Permission perm) {
        // throw new SecurityException("权限异常: " + perm.toString());
    }

    /**
     * 检查程序是否是可执行文件
     *
     * @param cmd the specified system command.
     */
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("chechExec 权限异常: " + cmd);
    }

    /**
     * 检查程序是否读文件
     *
     * @param file the system-dependent file name.
     */
    @Override
    public void checkRead(String file) {
        System.out.println(file);
        if (file.contains("D:\\IdeaProjects\\bestoj\\bestoj-code-sandbox")) {
            return;
        }
    }

    /**
     * 检查程序是否写文件
     *
     * @param file the system-dependent filename.
     */
    @Override
    public void checkWrite(String file) {
        throw new SecurityException("写文件权限异常：try to write " + file);
    }

    /**
     * 检查程序是否删除文件
     *
     * @param file the system-dependent filename.
     */
    @Override
    public void checkDelete(String file) {

    }

    /**
     * 检查程序是否允许连接网络
     *
     * @param host the host name port to connect to.
     * @param port the protocol port to connect to.
     */
    @Override
    public void checkConnect(String host, int port) {
        super.checkConnect(host, port);
    }
}
