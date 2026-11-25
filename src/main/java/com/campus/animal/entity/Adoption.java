package com.campus.animal.entity;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Adoption {
    private Integer id;
    private Integer animalId;
    private Integer applicantId;
    private String reason;
    private String contact;
    private String status = "待审核";
    private String reviewComment;
    private Integer reviewUserId;
    private LocalDateTime applyTime = LocalDateTime.now();
    private LocalDateTime reviewTime;
}
