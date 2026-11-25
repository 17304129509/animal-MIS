package com.campus.animal.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AdminDtos {
    private AdminDtos() {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record AnimalRequest(
            @NotBlank @Size(max = 20) String animalName,
            @NotBlank @Size(max = 10) String type,
            @Size(max = 20) String color, @Size(max = 10) String gender,
            @Size(max = 50) String location, @Size(max = 20) String health,
            @Size(max = 200) String remark) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record RescueRequest(
            Integer animalId, @NotBlank @Size(max = 300) String rescueContent,
            @NotBlank @Size(max = 50) String location, @Size(max = 10) String urgency) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record FeedPointRequest(
            @NotBlank @Size(max = 30) String pointName,
            @NotBlank @Size(max = 50) String location, String foodStatus) {}

    public record Decision(@Size(max = 500) String reason, @Size(max = 200) String result,
                           @Size(max = 500) String comment) {}
}
