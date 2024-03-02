package com.mogun.backend.controller.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SignInRequest {

    @JsonProperty(value = "user_email")
    private String userEmail;

    @JsonProperty(value = "user_pw")
    private String userPassword;
}
