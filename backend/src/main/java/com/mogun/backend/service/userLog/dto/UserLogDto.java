package com.mogun.backend.service.userLog.dto;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.userLog.userBodyFatLog.UserBodyFatLog;
import com.mogun.backend.domain.userLog.userHeightLog.UserHeightLog;
import com.mogun.backend.domain.userLog.userMuscleMassLog.UserMuscleMassLog;
import com.mogun.backend.domain.userLog.userWeightLog.UserWeightLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserLogDto {

    private int userKey;
    private String email;

    // Seongmin 닉네임 변경을 위한 userName 추가
    private String userName;

    private float heightBefore;
    private float weightBefore;
    private float muscleMassBefore;
    private float bodyFatBefore;

    private float heightAfter;
    private float weightAfter;
    private float muscleMassAfter;
    private float bodyFatAfter;

//    @Builder
//    public UserLogDto(int userKey, String email, float heightBefore, float weightBefore, float muscleMassBefore, float bodyFatBefore, float heightAfter, float weightAfter, float muscleMassAfter, float bodyFatAfter) {
//
//        this.userKey = userKey;
//        this.email = email;
//
//        this.heightBefore = heightBefore;
//        this.weightBefore = weightBefore;
//        this.muscleMassBefore = muscleMassBefore;
//        this.bodyFatBefore = bodyFatBefore;
//
//        this.heightAfter = heightAfter;
//        this.weightAfter = weightAfter;
//        this.muscleMassAfter = muscleMassAfter;
//        this.bodyFatAfter = bodyFatAfter;
//    }

    public UserHeightLog toHeightLogEntity(User user) {
        return UserHeightLog.builder()
                .user(user)
                .heightBefore(heightBefore)
                .heightAfter(heightAfter)
                .build();
    }

    public UserWeightLog toWeightLogEntity(User user) {
        return UserWeightLog.builder()
                .user(user)
                .weightBefore(weightBefore)
                .weightAfter(weightAfter)
                .build();
    }

    public UserMuscleMassLog toMuscleMassLogEntity(User user) {
        return UserMuscleMassLog.builder()
                .user(user)
                .muscleMassBefore(muscleMassBefore)
                .muscleMassAfter(muscleMassAfter)
                .build();
    }

    public UserBodyFatLog toBodyFatLogEntity(User user) {
        return UserBodyFatLog.builder()
                .user(user)
                .bodyFatBefore(bodyFatBefore)
                .bodyFatAfter(bodyFatAfter)
                .build();
    }
}
