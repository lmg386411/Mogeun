package com.mogun.backend.controller.routine;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.routine.request.AddPlanListRequest;
import com.mogun.backend.controller.routine.request.CommonRoutineRequest;
import com.mogun.backend.controller.routine.response.AddPlanListResponse;
import com.mogun.backend.controller.routine.response.PlanListResponse;
import com.mogun.backend.controller.routine.response.SimpleMusclePartResponse;
import com.mogun.backend.controller.routine.response.SimplePlanInfoResponse;
import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.userRoutine.UserRoutineService;
import com.mogun.backend.service.routine.userRoutinePlan.UserRoutinePlanService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Routine/Plan")
public class PlanController {

    private final AttachPartService attachPartService;
    private final UserRoutineService routineService;
    private final UserRoutinePlanService planService;

    @ApiOperation(value = "운동 추가 API", notes = "특정 루틴에 운동을 추가한다.")
    @PostMapping("/Add")
    public ApiResponse<Object> addPlan(@RequestBody CommonRoutineRequest request) {

        ServiceStatus<Object> result = planService.addPlan(RoutineDto.builder()
                .routineKey(request.getRoutineKey())
                .execKey(request.getExecKey())
                .setAmount(request.getSets())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "2개 이상의 운동 추가 API", notes = "특정 루틴에 여러 개의 운동을 추가한다.")
    @PostMapping("/AddAll")
    public ApiResponse<Object> addAllPlan(@RequestBody AddPlanListRequest request) {

        List<Integer> success = new ArrayList<>();
        List<Integer> fail = new ArrayList<>();

        for(Integer key: request.getExecKeys()) {
            ServiceStatus<Object> result = planService.addPlan(RoutineDto.builder()
                    .routineKey(request.getRoutineKey())
                    .execKey(key)
                    .setAmount(1)
                    .build());

            if(result.getStatus() != 100) {
                fail.add(key);
            } else {
                success.add(key);
            }
        }

        return ApiResponse.of(HttpStatus.ACCEPTED, "OK", AddPlanListResponse.builder().addedExec(success).failedExec(fail).build());
    }

    @ApiOperation(value = "운동 수정 API", notes = "특정 루틴의 운동을 수정한다.")
    @PutMapping("/Edit")
    public ApiResponse<Object> editPlan(@RequestBody AddPlanListRequest request) {

        ServiceStatus result = planService.editPlan(RoutineDto.builder()
            .routineKey(request.getRoutineKey()).build(), request.getExecKeys());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "운동 제거 API", notes = "특정 루틴의 운동을 제거한다.")
    public ApiResponse<Object> removePlan(@RequestParam("routine_key") int routineKey, @RequestParam("exec_key") int execKey) {

        ServiceStatus<Object> result = planService.removePlan(RoutineDto.builder()
                .routineKey(routineKey)
                .execKey(execKey)
                .build());

        return ApiResponse.postAndPutResponse(result, null);
    }

    @ApiOperation(value = "운동 확인 API", notes = "하나의 루틴에 대해 등록된 운동을 모두 조회한다.")
    @GetMapping("/ListAll")
    public ApiResponse<List<SimplePlanInfoResponse>> getAllPlan(@RequestParam("routine_key") int routineKey) {

        ServiceStatus<RoutineDto> routineResult = routineService.getRoutine(routineKey);
        if(routineResult.getStatus() == 200)
            return ApiResponse.badRequest(routineResult.getMessage());

        ServiceStatus<List<RoutineDto>> planResult = planService.getAllPlan(RoutineDto.builder()
                .routineKey(routineKey)
                .build());
        List<RoutineDto> list = planResult.getData();
        List<SimplePlanInfoResponse> responses = new ArrayList<>();

        for(RoutineDto item: list) {

            List<String> parts = attachPartService.getPartNameByExercise(item.getExec());
            Exercise exec = item.getExec();

            responses.add(SimplePlanInfoResponse.builder()
                    .planKey(item.getPlanKey())
                    .execKey(exec.getExecKey())
                    .execName(exec.getName())
                    .engName(exec.getEngName())
                    .mainPart(SimpleMusclePartResponse.builder()
                            .partName(exec.getMainPart().getPartName())
                            .imagePath(exec.getMainPart().getImagePath())
                            .build())
                    .musclePart(parts)
                    .imagePath(exec.getImagePath())
                    .build());
        }

//        for(RoutineDto item: list) {

//            List<String> parts = attachPartService.getAllPartNameByExercise(item.getExec());
            // Seongmin 사용 근육 가져오기
//            List<String> parts = attachPartService.getPartNameByExercise(item.getExec());
//
//            planList.add(SimplePlanInfoResponse.builder()
//                    .execKey(item.getExec().getExecKey())
//                    .execName(item.getExec().getName())
//                    .imagePath(item.getExec().getImagePath())
//                    .musclePart(parts)
//                    .engName(item.getExec().getEngName())
//                    .mainPart(item.getExec().getMainPart().getPartName())
//                    .build());
//        }
//
//        return ApiResponse.ok(PlanListResponse.builder()
//                .routineKey(dto.getRoutineKey())
//                .routineName(dto.getRoutineName())
//                .exercises(planList)
//                .build());

        return ApiResponse.ok(responses);
    }

}
