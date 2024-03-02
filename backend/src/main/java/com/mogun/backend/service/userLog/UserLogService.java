package com.mogun.backend.service.userLog;

import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.domain.userDetail.UserDetail;
import com.mogun.backend.domain.userDetail.repository.UserDetailRepository;
import com.mogun.backend.domain.userLog.userBodyFatLog.UserBodyFatLog;
import com.mogun.backend.domain.userLog.userBodyFatLog.repository.UserBodyFatLogRepository;
import com.mogun.backend.domain.userLog.userHeightLog.repository.UserHeightLogRepository;
import com.mogun.backend.domain.userLog.userMuscleMassLog.UserMuscleMassLog;
import com.mogun.backend.domain.userLog.userMuscleMassLog.repository.UserMuscleMassLogRepository;
import com.mogun.backend.domain.userLog.userWeightLog.repository.UserWeightLogRepository;

import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.userLog.dto.SimpleBodyFatLog;
import com.mogun.backend.service.userLog.dto.SimpleMuscleMassLog;
import com.mogun.backend.service.userLog.dto.UserLogDto;
import com.mogun.backend.service.userLog.dto.UserMuscleAndFatLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLogService {

    private final UserRepository userRepository;
    private final UserDetailRepository detailRepository;
    private final UserHeightLogRepository heightLogRepository;
    private final UserWeightLogRepository weightLogRepository;
    private final UserMuscleMassLogRepository muscleMassLogRepository;
    private final UserBodyFatLogRepository bodyFatLogRepository;


    public ServiceStatus<Object> changeHeight(UserLogDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());
        dto.setHeightBefore(detail.get().getHeight());
        heightLogRepository.save(dto.toHeightLogEntity(user.get()));
        detail.get().setHeight(dto.getHeightAfter());

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> changeWeight(UserLogDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());
        dto.setWeightBefore(detail.get().getWeight());
        weightLogRepository.save(dto.toWeightLogEntity(user.get()));
        detail.get().setWeight(dto.getWeightAfter());

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> changeMuscleMass(UserLogDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());
        dto.setMuscleMassBefore(detail.get().getMuscleMass());
        muscleMassLogRepository.save(dto.toMuscleMassLogEntity(user.get()));
        detail.get().setMuscleMass(dto.getMuscleMassAfter());

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> changeBodyFat(UserLogDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());
        dto.setBodyFatBefore(detail.get().getBodyFat());
        bodyFatLogRepository.save(dto.toBodyFatLogEntity(user.get()));
        detail.get().setBodyFat(dto.getBodyFatAfter());

        return ServiceStatus.okStatus();
    }

    // Seongmin Change/All API를 위한 서비스 추가
    public ServiceStatus<Object> changeAll(UserLogDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty() || user.get().getIsLeaved() == 'E')
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");

        if (dto.getUserName() != null && user.get().getName() != dto.getUserName()) {
            user.get().setName(dto.getUserName());
            userRepository.save(user.get());
        }

        Optional<UserDetail> detail = detailRepository.findById(user.get().getUserKey());

        if (dto.getHeightAfter() != detail.get().getHeight()) {
            dto.setHeightBefore(detail.get().getHeight());
            heightLogRepository.save(dto.toHeightLogEntity(user.get()));
            detail.get().setHeight(dto.getHeightAfter());
        }

        if (dto.getWeightAfter() != detail.get().getWeight()) {
            dto.setWeightBefore(detail.get().getWeight());
            weightLogRepository.save(dto.toWeightLogEntity(user.get()));
            detail.get().setWeight(dto.getWeightAfter());
        }

        if (dto.getMuscleMassAfter() != detail.get().getMuscleMass()) {
            dto.setMuscleMassBefore(detail.get().getMuscleMass());
            muscleMassLogRepository.save(dto.toMuscleMassLogEntity(user.get()));
            detail.get().setMuscleMass(dto.getMuscleMassAfter());
        }

        if (dto.getBodyFatAfter() != detail.get().getBodyFat()) {
            dto.setBodyFatBefore(detail.get().getBodyFat());
            bodyFatLogRepository.save(dto.toBodyFatLogEntity(user.get()));
            detail.get().setBodyFat(dto.getBodyFatAfter());
        }

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<UserMuscleAndFatLogDto> getLast10Logs(UserLogDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");

        List<UserMuscleMassLog> muscleMassLogList = muscleMassLogRepository.findLast10LogByUser(user.get());
        List<UserBodyFatLog> bodyFatLogList = bodyFatLogRepository.findLast10LogByUser(user.get());

        List<SimpleBodyFatLog> fatLogs = new ArrayList<>();
        List<SimpleMuscleMassLog> muscleLogs = new ArrayList<>();

        for(UserMuscleMassLog log: muscleMassLogList) {

            muscleLogs.add(SimpleMuscleMassLog.builder()
                    .muscleMass(log.getMuscleMassAfter())
                    .changedTime(log.getChangedTime())
                    .build());
        }
        for(UserBodyFatLog log: bodyFatLogList) {

            fatLogs.add(SimpleBodyFatLog.builder()
                    .bodyFat(log.getBodyFatAfter())
                    .changedTime(log.getChangedTime())
                    .build());
        }

        return ServiceStatus.<UserMuscleAndFatLogDto>builder()
                .status(100)
                .message("SUCCESS")
                .data(UserMuscleAndFatLogDto.builder()
                        .muscleMassLogList(muscleLogs)
                        .bodyFatLogList(fatLogs)
                        .build())
                .build();
    }
}
