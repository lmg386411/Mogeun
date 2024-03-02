package com.mogun.backend.controller.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserJoinRequest {

    @JsonProperty(value = "user_email")
    private String userEmail;

    @JsonProperty(value = "user_pw")
    private String userPassword;

    @JsonProperty(value = "user_name")
    private String userName;

    private String Gender;

    private float height;

    private float weight;

    @JsonProperty(value = "smm")
    private float muscleMass;

    @JsonProperty(value = "ffm")
    private float bodyFat;
}
