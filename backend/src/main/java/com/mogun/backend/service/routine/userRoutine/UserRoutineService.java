package com.mogun.backend.service.routine.userRoutine;

import com.mogun.backend.domain.routine.setDetail.repository.SetDetailRepository;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutine.repository.UserRoutineRepository;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.routine.dto.RoutineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoutineService {

    private final UserRepository userRepository;
    private final UserRoutineRepository routineRepository;
    private final UserRoutinePlanRepository planRepository;
    private final SetDetailRepository setDetailRepository;

    public ServiceStatus<RoutineDto> getRoutine(int routineKey) {

        Optional<UserRoutine> routine = routineRepository.findById(routineKey);
        if(routine.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록되지 않은 루틴");
        if(routine.get().getIsDeleted() == 'Y')
            return ServiceStatus.errorStatus("요청 오류: 삭제된 루틴");

        RoutineDto dto = RoutineDto.builder()
                .routineKey(routine.get().getRoutineKey())
                .routineName(routine.get().getRoutineName())
                .build();

        return ServiceStatus.<RoutineDto>builder()
                .status(100)
                .data(dto)
                .build();
    }

    public ServiceStatus<UserRoutine> createRoutine(RoutineDto dto, int userKey) {

        Optional<User> user = userRepository.findById(userKey);
        if(user.isEmpty() || user.get().getIsLeaved() != 'J')
            return ServiceStatus.errorStatus("요청 오류 등록되지 않거나 탈퇴한 회원");

        UserRoutine save = routineRepository.save(dto.toRoutineEntity(user.get()));

        return  ServiceStatus.<UserRoutine>builder()
                .status(100)
                .data(save)
                .build();
    }

    public ServiceStatus<Object> renameRoutine(RoutineDto dto) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());

        if(routine.isEmpty() || routine.get().getIsDeleted() == 'Y')
            return ServiceStatus.errorStatus("요청 오류: 등록된 루틴이 아님");

        routine.get().setRoutineName(dto.getRoutineName());
        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> deleteRoutine(RoutineDto dto) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        if(routine.isEmpty() || routine.get().getIsDeleted() == 'Y')
            return ServiceStatus.errorStatus("요청 오류: 등록된 루틴이 아님");

        routine.get().setIsDeleted('Y');
        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> getAllRoutine(int userKey) {

        List<RoutineDto> dtoList = new ArrayList<>();
        Optional<User> user = userRepository.findById(userKey);
        if(user.isEmpty() || user.get().getIsLeaved() != 'J')
            return ServiceStatus.errorStatus("요청 오류: 등록되지 않은 회원 정보");

        List<UserRoutine> result = routineRepository.findAllByUser(user.get());
        for(UserRoutine routine: result) {

            if(routine.getIsDeleted() == 'N')
                dtoList.add(RoutineDto.builder()
                        .routineKey(routine.getRoutineKey())
                        .routineName(routine.getRoutineName())
                        .build());
        }

        return ServiceStatus.builder()
                .status(100)
                .data(dtoList)
                .build();
    }
}
