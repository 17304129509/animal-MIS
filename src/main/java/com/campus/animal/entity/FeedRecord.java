package com.campus.animal.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedRecord {
    private Integer recordId;
    private Integer userId;
    private Integer pointId;
    private String foodType;
    private BigDecimal amount;
    private Integer quantity = 1;
    private String remark;
    private LocalDateTime feedTime = LocalDateTime.now();
}
