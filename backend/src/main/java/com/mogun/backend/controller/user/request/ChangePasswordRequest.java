package com.mogun.backend.controller.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @JsonProperty(value = "user_email")
    private String email;

    @JsonProperty(value = "password_before")
    private String oldPassword;

    @JsonProperty(value = "password_after")
    private String newPassword;

    @Builder
    public ChangePasswordRequest(String email, String oldPassword, String newPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

}
