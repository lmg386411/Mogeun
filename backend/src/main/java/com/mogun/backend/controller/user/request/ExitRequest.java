package com.mogun.backend.controller.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExitRequest {

    @JsonProperty(value = "user_email")
    private String userId;

    @JsonProperty(value = "user_pw")
    private String userPassword;
}
