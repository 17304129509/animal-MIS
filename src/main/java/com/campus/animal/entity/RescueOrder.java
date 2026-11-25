package com.campus.animal.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RescueOrder {
    private Integer rescueId;
    private Integer animalId;
    private Integer publishUser;
    private String rescueContent;
    private String location;
    private String orderStatus = "待处理";
    private Integer dealUser;
    private String result;
    private LocalDateTime createTime = LocalDateTime.now();
    private String urgency;
}
