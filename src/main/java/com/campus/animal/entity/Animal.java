package com.campus.animal.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Animal {
    private Integer animalId;
    private String animalName;
    private String type;
    private String color;
    private String location;
    private String status = "健康";
    private String photoUrl;
    private String remark;
    private Boolean isApproved = false;
    private Integer createUser;
    private LocalDateTime createTime = LocalDateTime.now();
    private String gender;
    private String health;
}
