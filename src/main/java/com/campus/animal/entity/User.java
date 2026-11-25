package com.campus.animal.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Integer userId;
    private String username;
    private String password;
    private String nickname;
    private String phone;
    private String role = "student";
    private Integer lovePoints = 100;
    private LocalDateTime createTime = LocalDateTime.now();
}
