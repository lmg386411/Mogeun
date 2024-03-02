package com.mogun.backend.controller.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class UserDetailResponse {

    // Seongmin 닉네임 추가
    @JsonProperty(value = "user_name")
    private String userName;
    private float height;
    private float weight;

    @JsonProperty(value = "muscle_mass")
    private float muscleMass;

    @JsonProperty(value = "body_fat")
    private float bodyFat;

    // Seongmin 닉네임 추가
    @Builder
    public UserDetailResponse(String userName, float height, float weight, float muscleMass, float bodyFat) {
        this.userName = userName;
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
    }

}
