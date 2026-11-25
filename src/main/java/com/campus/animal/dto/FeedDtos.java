package com.campus.animal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

public final class FeedDtos {
    private FeedDtos() {}

    @Getter
    @Setter
    public static class FeedRecordRequest {
        @NotNull
        private Integer pointId;
        private String foodType;
        private BigDecimal amount;
        private Integer quantity = 1;
        private String remark;
    }

    @Getter
    @Setter
    public static class FoodStatusRequest {
        @NotBlank
        private String foodStatus;
    }

    @Getter
    @Setter
    public static class FeedPointRequest {
        @NotBlank
        private String pointName;
        @NotBlank
        private String location;
        private String foodStatus;
    }
}
