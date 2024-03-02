package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.report.request.CommonReportRequest;
import com.mogun.backend.controller.report.request.MuscleActReportRequest;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.report.RoutineReportService;
import com.mogun.backend.service.report.SetReportService;
import com.mogun.backend.service.report.dto.MuscleActsDto;
import com.mogun.backend.service.report.dto.RoutineReportDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Report/Routine")
public class RoutineReportController {

    private final RoutineReportService routineReportService;
    private final SetReportService setReportService;

    @ApiOperation(value = "루틴 시작 API", notes = "하나의 특정 루틴을 시작한다.")
    @PostMapping("/Start")
    public ApiResponse<Object> startRoutineReport(@RequestBody CommonReportRequest request) {

        ServiceStatus<Long> result = routineReportService.startRoutineReport(RoutineReportDto.builder()
                .userKey(request.getUserKey())
                .routineKey(request.getRoutineKey())
                .isAttached(request.getIsAttached())
                .build());
        request.setReportKey(result.getData());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "루틴 종료 API", notes = "하나의 특정 루틴을 종료한다.")
    @PutMapping("/End")
    public ApiResponse<Object> endRoutineReport(@RequestBody CommonReportRequest request) {

        ServiceStatus<Object> result = routineReportService.endRoutineReport(RoutineReportDto.builder()
                .reportKey(request.getRoutineReportKey())
                .userKey(request.getUserKey())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "세트 기록 API", notes = "하나의 특정 운동의 1개 세트를 측정 및 기록한다.")
    @PostMapping("/Set")
    public ApiResponse<Object> insertSetReport(@RequestBody CommonReportRequest request) {

        List<MuscleActsDto> list = new ArrayList<>();
        List<MuscleActReportRequest> req = request.getActList();
        for(MuscleActReportRequest item: req) {
            list.add(MuscleActsDto.builder()
                    .sensorNumber(item.getSensorNumber())
                    .muscleAverage(item.getMuscleAverage())
                    .muscleFatigue(item.getMuscleFatigue())
                    .build()
            );
        }

        ServiceStatus<Object> result = setReportService.insertSetReport(RoutineReportDto.builder()
                .reportKey(request.getRoutineReportKey())
                .planKey(request.getPlanKey())
                .setNumber(request.getSetNumber())
                .weight(request.getWeight())
                .targetRep(request.getTargetRepeat())
                .successRep(request.getSuccessRepeat())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .actsDtoList(list)
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
