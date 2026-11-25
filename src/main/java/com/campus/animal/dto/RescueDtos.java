package com.campus.animal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public final class RescueDtos {
    private RescueDtos() {}

    @Getter
    @Setter
    public static class StatusUpdateRequest {
        @NotBlank
        private String orderStatus;
        private String result;
    }

    @Getter
    @Setter
    public static class RescueRequest {
        private Integer animalId;
        @NotBlank
        private String rescueContent;
        @NotBlank
        private String location;
    }
}
