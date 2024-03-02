package com.mogun.backend.controller.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class IsJoinedResponse {

    @JsonProperty(value = "is_joined")
    private boolean isJoined;

    @JsonProperty(value = "join_state")
    private String joinState;

    @Builder
    public IsJoinedResponse(boolean isJoined, String joinState) {
        this.isJoined = isJoined;
        this.joinState = joinState;
    }
}
