package com.mogun.backend.service.user;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.domain.userDetail.UserDetail;
import com.mogun.backend.domain.userDetail.repository.UserDetailRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailRepository userDetailRepository;

    // GET Request 는 dto 객체로 전달
    // POST, PUT, PATCH, DELETE Request 는 Result String 으로 반환 -> 성공 시 SUCCESS, 이외엔 실패 사유

    public ServiceStatus<Object> joinUser(UserDto dto) {

        char joinState = isJoined(dto.getEmail());
        char userGender = dto.getGender();

        if(joinState == 'J')
            return ServiceStatus.errorStatus("이미 등록된 이메일");
        if(joinState == 'E')
            return ServiceStatus.errorStatus("탈퇴한 회원");
        if(!(userGender== 'm' || userGender == 'f'))
            return ServiceStatus.errorStatus("지원 가능한 성별이 아님(m 혹은 f)");

        User savedUser = userRepository.save(dto.toEntity());
        UserDetail savedDetail = userDetailRepository.save(dto.toDetailedEntity(savedUser));

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> exitUser(String email, String password) {

        char joinState = isJoined(email);
        if(joinState == 'E')
            return ServiceStatus.errorStatus("요청 오류: 이미 탈퇴한 회원");

        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 잘못된 회원 정보");

        user.get().setIsLeaved('E');

        return ServiceStatus.okStatus();
    }

    public char isJoined(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(User::getIsLeaved).orElse('N');

    }

    public ServiceStatus<UserDto> getUserDetail(int userKey) {
        Optional<User> user = userRepository.findById(userKey);
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록되지 않은 회원");

        Optional<UserDetail> userDetail = userDetailRepository.findById(user.get().getUserKey());
        if(userDetail.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 회원 상세 정보가 없음");

        return ServiceStatus.<UserDto>builder()
                .status(100)
                .message("SUCCESS")
                .data(UserDto.builder()
                        .name(user.get().getName()) // Seongmin 닉네임 추가
                        .height(userDetail.get().getHeight())
                        .weight(userDetail.get().getWeight())
                        .muscleMass(userDetail.get().getMuscleMass())
                        .bodyFat(userDetail.get().getBodyFat())
                        .build())
                .build();
    }

    public ServiceStatus<Object> changePassword(String email, String oldPassword, String newPassword) {

        Optional<User> user = userRepository.findByEmailAndPassword(email, oldPassword);
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 잘못된 회원 정보");
        if(oldPassword.equals(newPassword))
            return ServiceStatus.errorStatus("요청 오류: 동일한 비밀번호");

        user.get().setPassword(newPassword);
        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> signIn(String email, String password) {

        Optional<User> user = userRepository.findByEmailAndPassword(email, password);

        if(user.isEmpty())
            return ServiceStatus.errorStatus("FAILED");
        if(user.get().getIsLeaved() == 'E')
            return ServiceStatus.errorStatus("FAILED");

        return ServiceStatus.builder()
                .status(100)
                .message("SUCCESS")
                .data(user.get().getUserKey())
                .build();
    }
}
