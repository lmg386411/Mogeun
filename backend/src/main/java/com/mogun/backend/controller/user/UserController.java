package com.mogun.backend.controller.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.user.request.ChangePasswordRequest;
import com.mogun.backend.controller.user.request.ExitRequest;
import com.mogun.backend.controller.user.request.SignInRequest;
import com.mogun.backend.controller.user.response.UserDetailResponse;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.user.UserService;
import com.mogun.backend.service.user.dto.UserDto;
import com.mogun.backend.controller.user.request.UserJoinRequest;
import com.mogun.backend.controller.user.response.IsJoinedResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/User")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "가입 여부 API", notes = "입력된 이메일 주소로 회원 등록이 되어 있는지 판별한다.")
    @GetMapping("/isJoined")
    public ApiResponse<IsJoinedResponse> isJoined(@RequestParam String email) {

        char joinState = userService.isJoined(email);
        if(joinState == 'J')
            return ApiResponse.ok(IsJoinedResponse.builder()
                    .isJoined(true)
                    .joinState("가입된 회원")
                    .build());
        else if(joinState == 'E')
            return ApiResponse.ok(IsJoinedResponse.builder()
                    .isJoined(false)
                    .joinState("탈퇴한 회원")
                    .build());
        else
            return ApiResponse.ok(IsJoinedResponse.builder()
                    .isJoined(false)
                    .joinState("가입하지 않은 회원")
                    .build());
    }

    @ApiOperation(value = "회원가입 API", notes = "회원 정보를 입력 후 회원 가입을 수행한다.")
    @PostMapping("/Enroll")
    public ApiResponse<Object> enrollUser(@RequestBody UserJoinRequest userJoinRequest) {
        ServiceStatus<Object> result = userService.joinUser(UserDto.builder()
                .email(userJoinRequest.getUserEmail())
                .password(userJoinRequest.getUserPassword())
                .name(userJoinRequest.getUserName())
                .gender(userJoinRequest.getGender().charAt(0))
                .height(userJoinRequest.getHeight())
                .weight(userJoinRequest.getWeight())
                .muscleMass(userJoinRequest.getMuscleMass())
                .bodyFat(userJoinRequest.getBodyFat())
                .build()
        );

        return ApiResponse.postAndPutResponse(result, userJoinRequest);
    }

    @ApiOperation(value = "회원 탈퇴 API", notes = "해당 서비스에서 회원을 탈퇴한다.")
    @PostMapping("/Exit")
    public ApiResponse<Object> exitUser(@RequestBody ExitRequest exitRequest) {

        ServiceStatus<Object> result = userService.exitUser(exitRequest.getUserId(), exitRequest.getUserPassword());

        return ApiResponse.postAndPutResponse(result, exitRequest);
    }

    @ApiOperation(value = "비밀번호 변경 API", notes = "회원의 로그인 비밀번호를 변경한다. ")
    @PostMapping("/Change/Password")
    public ApiResponse<Object> changePassword(@RequestBody ChangePasswordRequest pwdReq) {

        ServiceStatus<Object> result = userService.changePassword(pwdReq.getEmail(), pwdReq.getOldPassword(), pwdReq.getNewPassword());

        return ApiResponse.postAndPutResponse(result, pwdReq);
    }

    @ApiOperation(value = "회원 세부 정보 API", notes = "회원의 신체 정보를 조회 및 반환한다.")
    @GetMapping("/Detail")
    public ApiResponse<Object> getUserDetail(@RequestParam("user_key") int userKey) {

        ServiceStatus<UserDto> result = userService.getUserDetail(userKey);

        if(result.getStatus() != 100)
            return ApiResponse.badRequest(result.getMessage());
        UserDto data = result.getData();

        return ApiResponse.ok(UserDetailResponse.builder()
                .userName(data.getName())
                .height(data.getHeight())
                .weight(data.getWeight())
                .muscleMass(data.getMuscleMass())
                .bodyFat(data.getBodyFat())
                .build());
    }

    @ApiOperation(value = "로그인 API", notes = "회원 정보를 입력하여 로그인한다.")
    @PostMapping("/SignIn")
    public ApiResponse<Object> signIn(@RequestBody SignInRequest request) {

        ServiceStatus<Object> result = userService.signIn(request.getUserEmail(), request.getUserPassword());

        if(result.getStatus() != 100)
            return ApiResponse.of(HttpStatus.BAD_REQUEST, "FAILED", -1);

        return ApiResponse.of(HttpStatus.ACCEPTED, "SUCCESS", result.getData());
    }
}
