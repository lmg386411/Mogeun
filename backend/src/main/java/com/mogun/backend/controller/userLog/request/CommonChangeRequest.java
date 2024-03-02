package com.mogun.backend.controller.userLog.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class CommonChangeRequest {

    @JsonProperty("user_key")
    private int userKey;

//    @JsonProperty("user_email")
//    private String email;

    // Seongmin 닉네임을 입력 받기 위해 추가
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("weight")
    private float weight;

    @JsonProperty("height")
    private float height;

    @JsonProperty("muscle_mass")
    private float muscleMass;

    @JsonProperty("body_fat")
    private float bodyFat;

    // Seongmin email -> userKey, userName 추가
    @Builder
    public CommonChangeRequest(int userKey, String userName, float weight, float height, float muscleMass, float bodyFat) {

        this.userKey = userKey;
        this.userName = userName;
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;

    }

}
