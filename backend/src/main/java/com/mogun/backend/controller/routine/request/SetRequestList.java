package com.mogun.backend.controller.routine.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SetRequestList {

    @JsonProperty("plan_key")
    private int plan_key;

    @JsonProperty("set_info")
    private List<SetInfo> setInfoList;
}
