package com.campus.animal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public final class UserDtos {
    private UserDtos() {}

    @Getter
    @Setter
    public static class RoleUpdateRequest {
        @NotBlank
        private String role;
    }

    @Getter
    @Setter
    public static class ChangePasswordRequest {
        @NotBlank
        private String oldPassword;
        @NotBlank
        private String newPassword;
    }

    @Getter
    @Setter
    public static class UpdateInfoRequest {
        private String nickname;
        private String phone;
    }

    @Getter
    @Setter
    public static class LoginResponse {
        private String token;
        private Integer userId;
        private String username;
        private String nickname;
        private String role;
        private Integer lovePoints;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    public static class RegisterRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
        @NotBlank
        private String nickname;
        private String phone;
    }
}
