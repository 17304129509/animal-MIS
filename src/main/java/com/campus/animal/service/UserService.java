package com.campus.animal.service;

import com.campus.animal.common.BusinessException;
import com.campus.animal.common.PageResult;
import com.campus.animal.config.AuthContext;
import com.campus.animal.config.RoleCheck;
import com.campus.animal.dto.UserDtos;
import com.campus.animal.entity.User;
import com.campus.animal.mapper.UserMapper;
import com.campus.animal.util.Enums;
import com.campus.animal.util.JwtUtil;
import com.campus.animal.util.PageHelper;
import com.campus.animal.util.Passwords;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserDtos.LoginResponse register(UserDtos.RegisterRequest req) {
        if (this.userMapper.existsByUsername(req.getUsername())) {
            throw BusinessException.badRequest("用户名已存在");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(Passwords.encode(req.getPassword()));
        user.setNickname(req.getNickname());
        user.setPhone(req.getPhone());
        user.setRole("student");
        user.setLovePoints(100);
        userMapper.insert(user);
        return this.buildLoginResponse(user);
    }

    public UserDtos.LoginResponse login(UserDtos.LoginRequest req) {
        return authenticate(req, false);
    }

    public UserDtos.LoginResponse adminLogin(UserDtos.LoginRequest req) {
        return authenticate(req, true);
    }

    private UserDtos.LoginResponse authenticate(UserDtos.LoginRequest req, boolean admin) {
        String error = admin ? "账号或密码错误，或非管理员账号" : "用户名或密码错误";
        User user = userMapper.findByUsername(req.getUsername())
                .orElseThrow(() -> BusinessException.badRequest(error));
        if ((admin && !"admin".equals(user.getRole())) || !Passwords.matches(req.getPassword(), user.getPassword())) {
            throw BusinessException.badRequest(error);
        }
        if (Passwords.isLegacy(user.getPassword())) {
            userMapper.upgradePassword(user.getUserId(), user.getPassword(), Passwords.encode(req.getPassword()));
        }
        return this.buildLoginResponse(user);
    }

    public Map<String, Object> getCurrentUser() {
        RoleCheck.requireLogin();
        User user = this.userMapper.findById(AuthContext.getUserId()).orElseThrow(() -> BusinessException.notFound("用户不存在"));
        return UserService.toUserMap(user, false);
    }

    @Transactional
    public Map<String, Object> updateInfo(UserDtos.UpdateInfoRequest req) {
        RoleCheck.requireLogin();
        User user = this.userMapper.lockById(AuthContext.getUserId()).orElseThrow(() -> BusinessException.notFound("用户不存在"));
        if (req.getNickname() != null) {
            user.setNickname(req.getNickname());
        }
        if (req.getPhone() != null) {
            user.setPhone(req.getPhone());
        }
        userMapper.updateProfile(user);
        return UserService.toUserMap(user, false);
    }

    @Transactional
    public void changePassword(UserDtos.ChangePasswordRequest req) {
        RoleCheck.requireLogin();
        User user = this.userMapper.lockById(AuthContext.getUserId()).orElseThrow(() -> BusinessException.notFound("用户不存在"));
        if (!Passwords.matches(req.getOldPassword(), user.getPassword())) {
            throw BusinessException.badRequest("旧密码不正确");
        }
        user.setPassword(Passwords.encode(req.getNewPassword()));
        userMapper.updatePassword(user.getUserId(), user.getPassword());
    }

    public PageResult<Map<String, Object>> listUsers(String role, int page, int pageSize) {
        RoleCheck.requireRole("admin");
        PageHelper paging = PageHelper.of(page, pageSize);
        return paging.result(userMapper.findPage(role, paging.offset(), paging.pageSize()),
                userMapper.countFiltered(role), u -> UserService.toUserMap(u, false));
    }

    public void updateRole(Integer id, UserDtos.RoleUpdateRequest req) {
        RoleCheck.requireRole("admin");
        Enums.checkRole(req.getRole());
        User user = this.userMapper.findById(id).orElseThrow(() -> BusinessException.notFound("用户不存在"));
        user.setRole(req.getRole());
        userMapper.updateRole(user.getUserId(), user.getRole());
    }

    public User findById(Integer id) {
        return id == null ? null : userMapper.findById(id).orElse(null);
    }

    @Transactional
    public void deductLovePoints(Integer userId, Integer points) {
        User user = this.userMapper.lockById(userId).orElseThrow(() -> BusinessException.notFound("用户不存在"));
        if (user.getLovePoints() < points) {
            throw BusinessException.badRequest("爱心积分不足，当前积分: " + user.getLovePoints());
        }
        user.setLovePoints(user.getLovePoints() - points);
        userMapper.updatePoints(userId, user.getLovePoints());
    }

    private UserDtos.LoginResponse buildLoginResponse(User user) {
        UserDtos.LoginResponse resp = new UserDtos.LoginResponse();
        resp.setToken(this.jwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole()));
        resp.setUserId(user.getUserId());
        resp.setUsername(user.getUsername());
        resp.setNickname(user.getNickname());
        resp.setRole(user.getRole());
        resp.setLovePoints(user.getLovePoints());
        return resp;
    }

    public static Map<String, Object> toUserMap(User user, boolean includePassword) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("userId", user.getUserId());
        m.put("username", user.getUsername());
        m.put("nickname", user.getNickname());
        m.put("phone", user.getPhone());
        m.put("role", user.getRole());
        m.put("lovePoints", user.getLovePoints());
        m.put("createTime", user.getCreateTime());
        if (includePassword) {
            m.put("password", user.getPassword());
        }
        return m;
    }

    public UserService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }
}
