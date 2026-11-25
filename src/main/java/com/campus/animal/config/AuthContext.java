package com.campus.animal.config;

public final class AuthContext {
    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();

    private AuthContext() {
    }

    public static void set(Integer userId, String role) {
        USER_ID.set(userId);
        ROLE.set(role);
    }

    public static Integer getUserId() {
        return USER_ID.get();
    }

    public static String getRole() {
        return ROLE.get();
    }

    public static void clear() {
        USER_ID.remove();
        ROLE.remove();
    }
}
