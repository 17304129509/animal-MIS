package com.campus.animal.controller;

import com.campus.animal.common.ApiResponse;
import com.campus.animal.dto.UserDtos;
import com.campus.animal.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api"})
public class UserController {
    private final UserService userService;

    @PostMapping(value={"/user/register"})
    public ApiResponse<UserDtos.LoginResponse> register(@Valid @RequestBody UserDtos.RegisterRequest req) {
        return ApiResponse.ok(this.userService.register(req));
    }

    @PostMapping(value={"/user/login"})
    public ApiResponse<UserDtos.LoginResponse> login(@Valid @RequestBody UserDtos.LoginRequest req) {
        return ApiResponse.ok(this.userService.login(req));
    }

    @GetMapping(value={"/user/info"})
    public ApiResponse<Map<String, Object>> info() {
        return ApiResponse.ok(this.userService.getCurrentUser());
    }

    @PutMapping(value={"/user/info"})
    public ApiResponse<Map<String, Object>> updateInfo(@RequestBody UserDtos.UpdateInfoRequest req) {
        return ApiResponse.ok(this.userService.updateInfo(req));
    }

    @PutMapping(value={"/user/password"})
    public ApiResponse<Void> changePassword(@Valid @RequestBody UserDtos.ChangePasswordRequest req) {
        this.userService.changePassword(req);
        return ApiResponse.ok();
    }

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
