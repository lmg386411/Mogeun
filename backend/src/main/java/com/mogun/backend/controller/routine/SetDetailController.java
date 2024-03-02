package com.mogun.backend.controller.routine;


import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.request.SetInfo;
import com.mogun.backend.controller.routine.request.SetRequest;
import com.mogun.backend.controller.routine.request.SetRequestList;
import com.mogun.backend.controller.routine.response.AllSetInfoResponse;
import com.mogun.backend.domain.routine.setDetail.SetDetail;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.setDetail.SetDetailService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Routine/Set")
public class SetDetailController {

    private final SetDetailService setDetailService;

    @ApiOperation(value = "세트 추가 API", notes = "루틴 내 하나의 운동에 대해 세트 정보를 추가한다.")
    @PostMapping("/Add")
    public ApiResponse<Object> addOneSet(@RequestBody SetRequest request) {

        ServiceStatus<Object> result = setDetailService.addOneSetGoal(RoutineDto.builder()
                .planKey(request.getPlanKey())
                .setNumber(request.getSetNumber())
                .weight(request.getWeight())
                .targetRep(request.getTargetRep())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "2개 이상의 세트 추가 API", notes = "루틴 내 하나의 운동에 대해 세트 정보를 여러 개 추가한다.")
    @PostMapping("/AddAll")
    public ApiResponse<Object> addAllSet(@RequestBody SetRequestList requestList) {

        List<RoutineDto> dtoList = new ArrayList<>();

        for(SetInfo info: requestList.getSetInfoList()) {

            dtoList.add(RoutineDto.builder()
                    .planKey(requestList.getPlan_key())
                    .setNumber(info.getSetNumber())
                    .weight(info.getWeight())
                    .targetRep(info.getTargetRep())
                    .build());
        }

        ServiceStatus<Object> result = setDetailService.addAllSetGoal(dtoList);

        return ApiResponse.postAndPutResponse(result, requestList);
    }

    @ApiOperation(value = "전체 세트 조회 API", notes = "루틴 내 하나의 운동에 대해 모든 세트 정보를 조회한다.")
    @GetMapping("/ListAll")
    public ApiResponse<Object> getAll(@RequestParam("plan_key") int planKey) {

        List<SetInfo> infoList = new ArrayList<>();
        List<SetDetail> setDetailList = setDetailService.getAllSetInfo(RoutineDto.builder()
                .planKey(planKey)
                .build());

        if(setDetailList.isEmpty())
            return ApiResponse.badRequest("No data for this parameter");

        if(setDetailList.get(0).getSetKey() == -1)
            return ApiResponse.badRequest("요청 오류: 등록된 적 없는 운동 계획");

        String execName = setDetailList.get(0).getUserRoutinePlan().getExercise().getName();

        for(SetDetail item: setDetailList) {
            infoList.add(SetInfo.builder()
                    .setNumber(item.getSetNumber())
                    .weight(item.getWeight())
                    .targetRep(item.getTargetRepeat())
                    .build());
        }

        return ApiResponse.ok(AllSetInfoResponse.builder()
                .execName(execName)
                .setAmount(setDetailList.size())
                .setInfoList(infoList)
                .build());
    }

    @ApiOperation(value = "세트 제거 API", notes = "루틴 내 하나의 운동에 대해 세트를 제거한다.")
    @DeleteMapping("/Delete")
    public ApiResponse<Object> deleteOneSet(@RequestBody CommonRoutineRequest request) {

        ServiceStatus<Object> result = setDetailService.deleteOneSet(RoutineDto.builder().setKey(request.getSetKey()).build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "전체 세트 제거 API", notes = "루틴 내 하나의 운동에 대해 모든 세트 정보를 제거한다.")
    @DeleteMapping("/DeleteAll")
    public ApiResponse<Object> deleteAll(@RequestParam("plan_key") int planKey) {

        ServiceStatus<Object> result = setDetailService.deleteAllSet(RoutineDto.builder().planKey(planKey).build());

        return ApiResponse.postAndPutResponse(result, planKey);
    }
}
