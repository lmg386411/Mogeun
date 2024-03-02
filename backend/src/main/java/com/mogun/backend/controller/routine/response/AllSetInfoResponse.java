package com.mogun.backend.controller.routine.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogun.backend.controller.routine.request.SetInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AllSetInfoResponse {

    @JsonProperty("exec_name")
    private String execName;

    @JsonProperty("set_amount")
    private int setAmount;

    @JsonProperty("set_details")
    List<SetInfo> setInfoList;
}
