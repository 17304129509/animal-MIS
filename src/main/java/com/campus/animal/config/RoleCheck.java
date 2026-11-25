package com.campus.animal.config;

import com.campus.animal.common.BusinessException;
import com.campus.animal.config.AuthContext;
import java.util.Arrays;
import java.util.Set;

public final class RoleCheck {
    private RoleCheck() {
    }

    public static void requireLogin() {
        if (AuthContext.getUserId() == null) {
            throw BusinessException.forbidden("请先登录");
        }
    }

    public static void requireRole(String ... roles) {
        RoleCheck.requireLogin();
        String role = AuthContext.getRole();
        Set<String> allowed = Set.of(roles);
        if (!allowed.contains(role)) {
            throw BusinessException.forbidden("无权限执行此操作");
        }
    }

    public static boolean hasRole(String ... roles) {
        String role = AuthContext.getRole();
        return role != null && Arrays.asList(roles).contains(role);
    }
}
