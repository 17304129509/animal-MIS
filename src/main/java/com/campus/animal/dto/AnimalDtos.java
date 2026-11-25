package com.campus.animal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public final class AnimalDtos {
    private AnimalDtos() {}

    @Getter
    @Setter
    public static class ApproveRequest {
        private Boolean approved;
    }

    @Getter
    @Setter
    public static class StatusRequest {
        @NotBlank
        private String status;
    }

    @Getter
    @Setter
    public static class AnimalRequest {
        @NotBlank
        private String animalName;
        @NotBlank
        private String type;
        private String color;
        @NotBlank
        private String location;
        private String status;
        private String photoUrl;
        private String remark;
    }
}
