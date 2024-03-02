package com.mogun.backend.controller.userLog;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.userLog.request.CommonChangeRequest;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.userLog.UserLogService;
import com.mogun.backend.service.userLog.dto.UserLogDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/User/Log")
public class UserLogController {

    private final UserLogService logService;

    @ApiOperation(value = "신장 변경 API", notes = "회원의 신장 정보를 변경한다.")
    @PostMapping("/Change/Height")
    public ApiResponse<Object> changeHeight(@RequestBody CommonChangeRequest request) {

        ServiceStatus<Object> result = logService.changeHeight(UserLogDto.builder()
                .userKey(request.getUserKey())
                .heightAfter(request.getHeight())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "체중 변경 API", notes = "회원의 체중 정보를 변경한다.")
    @PostMapping("/Change/Weight")
    public ApiResponse<Object> changeWeight(@RequestBody CommonChangeRequest request) {

        ServiceStatus<Object> result = logService.changeWeight(UserLogDto.builder()
                .userKey(request.getUserKey())
                .weightAfter(request.getWeight())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "골격근 변경 API", notes = "회원의 골격근 정보를 변경한다.")
    @PostMapping("/Change/MuscleMass")
    public ApiResponse<Object> changeMuscleMass(@RequestBody CommonChangeRequest request) {

        ServiceStatus<Object> result = logService.changeMuscleMass(UserLogDto.builder()
                .userKey(request.getUserKey())
                .muscleMassAfter(request.getMuscleMass())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "체지방 변경 API", notes = "회원의 체지방 정보를 변경한다.")
    @PostMapping("/Change/BodyFat")
    public ApiResponse<Object> changeBodyFat(@RequestBody CommonChangeRequest request) {

        ServiceStatus<Object> result = logService.changeBodyFat(UserLogDto.builder()
                .userKey(request.getUserKey())
                .bodyFatAfter(request.getBodyFat())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    // Seongmin Change/All API 추가
    @ApiOperation(value = "전 정보 API", notes = "회원의 모든 신체 정보를 변경한다.")
    @PutMapping("/Change/All")
    public ApiResponse<Object> changeAll(@RequestBody CommonChangeRequest request) {

        ServiceStatus<Object> result = logService.changeAll(UserLogDto.builder()
                .userKey(request.getUserKey())
                .userName(request.getUserName())
                .heightAfter(request.getHeight())
                .weightAfter(request.getWeight())
                .muscleMassAfter(request.getMuscleMass())
                .bodyFatAfter(request.getBodyFat())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }
}
