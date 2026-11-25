package com.campus.animal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public final class AdoptionDtos {
    private AdoptionDtos() {}

    @Getter
    @Setter
    public static class ReviewRequest {
        @NotBlank
        private String status;
        private String reviewComment;
    }

    @Getter
    @Setter
    public static class ApplyRequest {
        @NotNull
        private Integer animalId;
        @NotBlank
        private String reason;
        private String contact;
    }
}
