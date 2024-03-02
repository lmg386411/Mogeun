package com.mogun.backend.service.routine.userRoutinePlan;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import com.mogun.backend.domain.routine.setDetail.repository.SetDetailRepository;
import com.mogun.backend.domain.routine.userRoutine.UserRoutine;
import com.mogun.backend.domain.routine.userRoutine.repository.UserRoutineRepository;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.routine.dto.RoutineDto;
import com.mogun.backend.service.routine.dto.RoutineOutlineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoutinePlanService {

    private final UserRoutineRepository routineRepository;
    private final UserRoutinePlanRepository planRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final SetDetailRepository setDetailRepository;

    public ServiceStatus<Object> addPlan(RoutineDto dto) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        if(routine.isEmpty() || routine.get().getIsDeleted() == 'Y')
            return ServiceStatus.errorStatus("요청 오류: 등록된 루틴이 아님");

        Optional<Exercise> exec = exerciseRepository.findById(dto.getExecKey());
        if(exec.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 추가 가능한 운동이 없음");

        planRepository.save(dto.toRoutinePlanEntity(routine.get(), exec.get()));

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> editPlan(RoutineDto dto, List<Integer> editedPlanList) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        if(routine.isEmpty() || routine.get().getIsDeleted() == 'Y')
            return ServiceStatus.errorStatus("요청 오류: 등록된 루틴이 아님");

        List<UserRoutinePlan>  existingPlanList = planRepository.findAllByUserRoutine(routine.get());

        // 방안 1. 삭제할 plan과 새로 등록할 plan을 구별 후 작업
        List<UserRoutinePlan> deleted = new ArrayList<>();
        List<Integer> added = new ArrayList<>();

        for(UserRoutinePlan plan: existingPlanList) {
            int flag = 0;
            for(Integer edit: editedPlanList) {
                if(plan.getExercise().getExecKey() == edit) {
                    flag = 1;
                    break;
                }
            }
            if(flag == 0)
                deleted.add(plan);
        }

        for(Integer edit: editedPlanList) {
            int flag = 0;
            for(UserRoutinePlan plan: existingPlanList) {
                if(plan.getExercise().getExecKey() == edit) {
                    flag = 1;
                    break;
                }
            }
            if(flag == 0)
                added.add(edit);
        }
        for(UserRoutinePlan del: deleted) {
            setDetailRepository.deleteAllByUserRoutinePlan(del);
            planRepository.delete(del);
        }

        for(Integer add: added)
            planRepository.save(UserRoutinePlan.builder()
                    .userRoutine(routine.get())
                    .user(routine.get().getUser())
                    .exercise(exerciseRepository.findById(add).get())
                    .setAmount(1)
                    .build());

        // 방안 2. 완전 삭제 후 재등록
//        for(UserRoutinePlan plan: existingPlanList) {
//            setDetailRepository.deleteAllByUserRoutinePlan(plan);
//        }
//
//        planRepository.deleteAllByUserRoutine(routine.get());
//
//        for(int edit: editedPlanList) {
//            planRepository.save(UserRoutinePlan.builder()
//                    .userRoutine(routine.get())
//                    .user(routine.get().getUser())
//                    .exercise(exerciseRepository.findById(edit).get())
//                    .setAmount(1)
//                    .build());
//        }

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> removePlan(RoutineDto dto) {

        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        Optional<Exercise> exercise = exerciseRepository.findById(dto.getExecKey());

        if(routine.isEmpty())
            return  ServiceStatus.errorStatus("요청 오류: 등록된 루틴이 아님");
        if(exercise.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 목록에 없는 운동");

        Optional<UserRoutinePlan> plan = planRepository.findByUserRoutineAndExercise(routine.get(), exercise.get());
        if(plan.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 적합한 운동 계획이 없음");
        planRepository.delete(plan.get());

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<List<RoutineDto>> getAllPlan(RoutineDto dto) {

        List<RoutineDto> result = new ArrayList<>();
        Optional<UserRoutine> routine = routineRepository.findById(dto.getRoutineKey());
        if(routine.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 루틴이 없음");
        if(routine.get().getIsDeleted() == 'Y')
            return ServiceStatus.errorStatus("요청 오류: 삭제된 루틴");

        List<UserRoutinePlan> planList = planRepository.findAllByUserRoutine(routine.get());

        for(UserRoutinePlan plan: planList) {
            result.add(RoutineDto.builder()
                    .planKey(plan.getRoutinePlanKey())
                    .exec(plan.getExercise())
                    .build());
        }

        return ServiceStatus.<List<RoutineDto>>builder()
                .status(100)
                .data(result)
                .build();
    }

    public ServiceStatus<List<RoutineOutlineDto>> getAllRoutineAndMuscle(RoutineDto dto) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록되지 않은 회원");

        List<UserRoutine> routineList = routineRepository.findAllByUser(user.get());
        List<UserRoutinePlan> planList = planRepository.findAllByUser(user.get());
        List<RoutineOutlineDto> routineOutlineDtoList = new ArrayList<>();

        int currentIndex = 0;

        // 모든 routine에 대해 목록 item 생성
        for(UserRoutine routine: routineList) {
            routineOutlineDtoList.add(RoutineOutlineDto.builder()
                    .name(routine.getRoutineName())
                    .routineKey(routine.getRoutineKey())
                    .muscleImagePathList(new HashSet<>())
                    .build());

            if(routine.getIsDeleted() == 'Y') continue;
            if(planList.isEmpty()) continue;
            if(currentIndex >= planList.size()) continue;
            int lastIndex = routineOutlineDtoList.size() - 1;

            while (planList.get(currentIndex).getUserRoutine().getRoutineKey() <= routine.getRoutineKey()) {

                UserRoutine nowRoutine = planList.get(currentIndex).getUserRoutine();
                if(nowRoutine.getRoutineKey() == routine.getRoutineKey())
                    routineOutlineDtoList.get(lastIndex).getMuscleImagePathList().add(planList.get(currentIndex).getExercise().getMainPart().getImagePath());

                currentIndex++;
                if(currentIndex >= planList.size()) break;
            }
        }

        return ServiceStatus.<List<RoutineOutlineDto>>builder()
                .status(100)
                .data(routineOutlineDtoList)
                .build();
    }
}
