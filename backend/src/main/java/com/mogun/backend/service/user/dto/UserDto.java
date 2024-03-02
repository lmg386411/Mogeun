package com.mogun.backend.service.user.dto;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.userDetail.UserDetail;
import com.mogun.backend.domain.userLog.userWeightLog.UserWeightLog;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class UserDto {

    // 회원 기본 정보
    private int userKey;
    private String email;
    private String password;
    private String name;
    private char gender;

    // 회원 상세 정보
    private float weight;
    private float height;
    private float muscleMass;
    private float bodyFat;

    @Builder
    public UserDto(String email, String password, String name, char gender, float weight, float height, float muscleMass, float bodyFat) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .gender(gender)
                .build();
    }

    public UserDetail toDetailedEntity(User user) {
        return UserDetail.builder()
                .user(user)
                .weight(weight)
                .height(height)
                .muscleMass(muscleMass)
                .bodyFat(bodyFat)
                .build();
    }
}
