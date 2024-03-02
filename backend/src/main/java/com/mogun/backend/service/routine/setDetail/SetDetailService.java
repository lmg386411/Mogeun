package com.mogun.backend.service.routine.setDetail;

import com.mogun.backend.domain.routine.setDetail.SetDetail;
import com.mogun.backend.domain.routine.setDetail.repository.SetDetailRepository;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutine.repository.UserRoutineRepository;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SetDetailService {

    private final SetDetailRepository setDetailRepository;
    private final UserRepository userRepository;
    private final UserRoutineRepository routineRepository;
    private final UserRoutinePlanRepository planRepository;

    public ServiceStatus<Object> addOneSetGoal(RoutineDto dto) {

        Optional<UserRoutinePlan> plan = planRepository.findById(dto.getPlanKey());
        if(plan.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록된 운동이 아님");

        setDetailRepository.save(dto.toSetDetailEntity(plan.get()));

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> addAllSetGoal(List<RoutineDto> dtoList) {

        Optional<UserRoutinePlan> plan = planRepository.findById(dtoList.get(0).getPlanKey());
        if(plan.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록된 운동이 아님");

        for(RoutineDto dto: dtoList) {
            setDetailRepository.save(dto.toSetDetailEntity(plan.get()));
        }

        return ServiceStatus.okStatus();
    }

    public List<SetDetail> getAllSetInfo(RoutineDto dto) {

        Optional<UserRoutinePlan> plan = planRepository.findById(dto.getPlanKey());
        if(plan.isEmpty()) {
            List<SetDetail> list = new ArrayList<>();
            list.add(SetDetail.builder().setKey(-1).build());
            return list;
        }

        return setDetailRepository.findAllByUserRoutinePlan(plan.get());
    }

    public ServiceStatus<Object> deleteOneSet(RoutineDto dto) {

        setDetailRepository.deleteById(dto.getSetKey());

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> deleteAllSet(RoutineDto dto) {

        Optional<UserRoutinePlan> plan = planRepository.findById(dto.getPlanKey());
        if(plan.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록된 운동이 아님");

        setDetailRepository.deleteAllByUserRoutinePlan(plan.get());

        return ServiceStatus.okStatus();
    }
}
