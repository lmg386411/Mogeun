package com.mogun.backend.controller.report;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.report.SetReportService;
import com.mogun.backend.service.report.dto.*;
import com.mogun.backend.service.userLog.UserLogService;
import com.mogun.backend.service.userLog.dto.UserLogDto;
import com.mogun.backend.service.userLog.dto.UserMuscleAndFatLogDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Summary")
public class UserSummaryController {

    private final UserLogService logService;
    private final SetReportService setReportService;

    @ApiOperation(value = "최신 신체 정보 조회 API", notes = "회원의 최근 체지방 및 골격근량에 대한 변화를 최대 10개까지 조회한다.")
    @GetMapping("/LastLogs")
    public ApiResponse<Object> getLast10BodyFatAndMuscleMassLogs(@RequestParam("user_key") int userKey) {

        ServiceStatus<UserMuscleAndFatLogDto> result = logService.getLast10Logs(UserLogDto.builder()
                .userKey(userKey)
                .build());

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }

    @ApiOperation(value = "가장 많이 수행한 운동 조회 API", notes = "옵션에 따른 전체, 연간, 월간 가장 많이 수행한 운동을 확인한다.")
    @GetMapping("/ExerciseMost")
    public ApiResponse<Object> getMostExercised(@RequestParam("user_key")int userKey, @RequestParam("search_type")int option) {

        ServiceStatus<MostPerformedDto> result = setReportService.mostPerformedExercise(RoutineReportDto.builder()
                .userKey(userKey).build(), option);

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }

    @ApiOperation(value = "가장 큰 중량 운동 조회 API", notes = "옵션에 따른 전체, 연간, 월간 가장 큰 중량을 수행한 운동을 확인한다.")
    @GetMapping("/ExerciseWeight")
    public ApiResponse<Object> getMostWeighted(@RequestParam("user_key")int userKey, @RequestParam("search_type")int option) {

        ServiceStatus<MostWeightDto> result = setReportService.mostWeightedExercise(RoutineReportDto.builder()
                .userKey(userKey).build(), option);

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }

    @ApiOperation(value = "가장 많은 세트 운동 조회 API", notes = "옵션에 따른 전체, 연간, 월간 가장 많은 세트를 수행한 운동을 확인한다.")
    @GetMapping("/ExerciseSet")
    public ApiResponse<Object> getMostSet(@RequestParam("user_key")int userKey, @RequestParam("search_type")int option) {

        ServiceStatus<MostSetsDto> result = setReportService.mostSetExercise(RoutineReportDto.builder()
                .userKey(userKey).build(), option);

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }

    @ApiOperation(value = "수행한 운동에 대한 근육 리스트 API", notes = "옵션에 따른 전체, 연간, 월간 수행한 운동 중 자극된 근육 부위를 모두 확인한다.")
    @GetMapping("/ExerciseMuscle")
    public ApiResponse<Object> getExecMuscle(@RequestParam("user_key")int userKey, @RequestParam("search_type")int option) {

        ServiceStatus<List<ExerciseMuscleDto>> result = setReportService.execMuscle(RoutineReportDto.builder()
                .userKey(userKey).build(), option);

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());

        return ApiResponse.ok(result.getData());
    }
}
