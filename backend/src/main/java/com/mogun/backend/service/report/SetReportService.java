package com.mogun.backend.service.report;

import com.mogun.backend.domain.attachPart.AttachPart;
import com.mogun.backend.domain.attachPart.repository.AttachPartRepository;
import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import com.mogun.backend.domain.report.muscleActInSet.MuscleActInSetLog;
import com.mogun.backend.domain.report.muscleActInSet.repository.MuscleActInSetLogRepository;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.routineReport.repository.RoutineReportRepository;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.domain.report.setReport.repository.ExecCountInterface;
import com.mogun.backend.domain.report.setReport.repository.ExecSetInterface;
import com.mogun.backend.domain.report.setReport.repository.ExecWeightInterface;
import com.mogun.backend.domain.report.setReport.repository.SetReportRepository;
import com.mogun.backend.domain.routine.userRoutinePlan.UserRoutinePlan;
import com.mogun.backend.domain.routine.userRoutinePlan.repository.UserRoutinePlanRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.exercise.ExerciseService;
import com.mogun.backend.service.report.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SetReportService {

    private final SetReportRepository setReportRepository;
    private final RoutineReportRepository routineReportRepository;
    private final UserRoutinePlanRepository planRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final AttachPartService attachPartService;
    private final AttachPartRepository attachPartRepository;
    private final MuscleActInSetLogRepository actInSetLogRepository;

    public ServiceStatus<Object> insertSetReport(RoutineReportDto dto) {

        Optional<RoutineReport> report = routineReportRepository.findById(dto.getReportKey());
        if(report.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 루틴 로그가 없음");

        Optional<UserRoutinePlan> plan = planRepository.findById(dto.getPlanKey());
        if(plan.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 회원의 루틴 혹은 운동 계획이 없음");

        if(!report.get().getUser().equals(plan.get().getUser()))
            return ServiceStatus.errorStatus("요청 오류: 로그 요청 회원과 루틴 소유 회원 불일치");

        SetReport save = setReportRepository.save(dto.toSetReportEntity(report.get(), plan.get().getExercise()));
        System.out.println(save.getSetReportKey());

        List<MuscleActsDto> dtoList = dto.getActsDtoList();
        for(MuscleActsDto act: dtoList) {
            Optional<AttachPart> sensing = attachPartRepository.findMainPartByExercise(save.getExercise());
            actInSetLogRepository.save(MuscleActInSetLog.builder()
                    .setReport(save)
                    .sensorNumber(act.getSensorNumber())
                    .musclePart(sensing.get().getMusclePart())
                    .muscleActivity(act.getMuscleAverage())
                    .muscleFatigue(act.getMuscleFatigue())
                    .build());
        }

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<MostPerformedDto> mostPerformedExercise(RoutineReportDto dto, int option) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록되지 않은 회원");

        LocalDateTime startDate;
        LocalDateTime now = LocalDateTime.now();
        if(option == 1)
            startDate = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        else if(option == 2)
            startDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        else if(option == 3)
            startDate = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
        else
            return ServiceStatus.errorStatus("요청 오류: 올바르지 않은 날짜 옵션");

        List<ExecCountInterface> countDtoList = setReportRepository.findRangedExerciseByUserAndStartDate(user.get(), startDate);
        if(countDtoList.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 조건에 부합하는 세트 기록이 없음");
        Optional<Exercise> exec = exerciseRepository.findById(countDtoList.get(0).getExec_Key());

        return ServiceStatus.<MostPerformedDto>builder()
                .status(100)
                .message("SUCCESS")
                .data(MostPerformedDto.builder()
                        .execName(exec.get().getName())
                        .imagePath(exec.get().getImagePath())
                        .performCount(countDtoList.get(0).getExec_Count())
                        .build())
                .build();
    }

    public ServiceStatus<MostWeightDto> mostWeightedExercise(RoutineReportDto dto, int option) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록되지 않은 회원");

        LocalDateTime startDate;
        LocalDateTime now = LocalDateTime.now();
        if(option == 1)
            startDate = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        else if(option == 2)
            startDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        else if(option == 3)
            startDate = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
        else
            return ServiceStatus.errorStatus("요청 오류: 올바르지 않은 날짜 옵션");

        List<ExecWeightInterface> weightDtoList = setReportRepository.findRangedWeightByUserAndStartDate(user.get(), startDate);
        if(weightDtoList.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 조건에 부합하는 세트 기록이 없음");
        Optional<Exercise> exec = exerciseRepository.findById(weightDtoList.get(0).getExec_Key());

        return ServiceStatus.<MostWeightDto>builder()
                .status(100)
                .message("SUCCESS")
                .data(MostWeightDto.builder()
                        .execName(exec.get().getName())
                        .imagePath(exec.get().getImagePath())
                        .weight(weightDtoList.get(0).getTrain_weight())
                        .build())
                .build();
    }

    public ServiceStatus<MostSetsDto> mostSetExercise(RoutineReportDto dto, int option) {

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록되지 않은 회원");

        LocalDateTime startDate;
        LocalDateTime now = LocalDateTime.now();
        if(option == 1)
            startDate = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        else if(option == 2)
            startDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        else if(option == 3)
            startDate = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
        else
            return ServiceStatus.errorStatus("요청 오류: 올바르지 않은 날짜 옵션");

        List<ExecSetInterface> weightDtoList = setReportRepository.findRangedSetByUserAndStartDate(user.get(), startDate);
        if(weightDtoList.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 조건에 부합하는 세트 기록이 없음");

        Optional<Exercise> exec = exerciseRepository.findById(weightDtoList.get(0).getExec_Key());

        return ServiceStatus.<MostSetsDto>builder()
                .status(100)
                .message("SUCCESS")
                .data(MostSetsDto.builder()
                        .execName(exec.get().getName())
                        .imagePath(exec.get().getImagePath())
                        .setCount(weightDtoList.get(0).getSet_Number())
                        .build())
                .build();
    }

    public ServiceStatus<List<ExerciseMuscleDto>> execMuscle(RoutineReportDto dto, int option) {

        List<ExerciseMuscleDto> list = new ArrayList<>();
        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록되지 않은 회원");

        LocalDateTime startDate;
        LocalDateTime now = LocalDateTime.now();
        if(option == 1)
            startDate = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        else if(option == 2)
            startDate = LocalDateTime.of(now.getYear(), 1, 1, 0, 0, 0);
        else if(option == 3)
            startDate = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0);
        else
            return ServiceStatus.errorStatus("요청 오류: 올바르지 않은 날짜 옵션");

        List<ExecCountInterface> countDtoList = setReportRepository.findRangedExerciseByUserAndStartDate(user.get(), startDate);
        if(countDtoList.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 조건에 부합하는 세트 기록이 없음");

        for(ExecCountInterface item: countDtoList) {

            Optional<Exercise> exec = exerciseRepository.findById(item.getExec_Key());
            List<String> parts = attachPartService.getMainSubPartNameByExercise(exec.get());

            list.add(ExerciseMuscleDto.<ExerciseMuscleDto>builder()
                    .execName(exec.get().getName())
                    .execParts(parts)
                    .build());
        }

        return ServiceStatus.<List<ExerciseMuscleDto>>builder()
                .status(100)
                .message("SUCCESS")
                .data(list)
                .build();
    }
}
