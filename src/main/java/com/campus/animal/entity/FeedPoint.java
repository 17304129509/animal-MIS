package com.campus.animal.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedPoint {
    private Integer pointId;
    private String pointName;
    private String location;
    private String foodStatus = "充足";
    private Integer lastUpdateUser;
    private LocalDateTime updateTime = LocalDateTime.now();
}
