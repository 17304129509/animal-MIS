package com.campus.animal.config;

import com.campus.animal.common.BusinessException;
import com.campus.animal.mapper.UserMapper;
import com.campus.animal.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private static final Set<String> LOGIN_PATHS =
            Set.of("/api/user/login", "/api/user/register", "/api/admin/login");
    private static final Pattern PUBLIC_GET = Pattern.compile(
            "^/api/(animal(/\\d+)?|feed-point(/\\d+)?|rescue(/\\d+)?|rank/(feed|rescue)|feed-record/(point/\\d+|stats))$");
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthContext.clear();
        String path = request.getServletPath();
        if ("POST".equals(request.getMethod()) && LOGIN_PATHS.contains(path)) {
            return true;
        }

        boolean admin = path.startsWith("/api/admin/");
        boolean optional = "GET".equals(request.getMethod()) && PUBLIC_GET.matcher(path).matches();
        String auth = request.getHeader("Authorization");
        boolean bearer = auth != null && auth.startsWith("Bearer ");
        if (bearer) {
            Integer userId = subject(auth.substring(7));
            if (userId != null) {
                userMapper.findById(userId).ifPresent(user -> AuthContext.set(userId, user.getRole()));
            }
        }
        if (!optional && AuthContext.getUserId() == null) {
            throw new BusinessException(admin ? 401 : 403, bearer ? "登录已过期，请重新登录" : "请先登录");
        }
        if (admin && !RoleCheck.hasRole("admin")) {
            AuthContext.clear();
            throw BusinessException.forbidden("仅管理员可操作");
        }
        return true;
    }

    private Integer subject(String token) {
        try {
            return Integer.valueOf(jwtUtil.parseToken(token).getSubject());
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }
}
