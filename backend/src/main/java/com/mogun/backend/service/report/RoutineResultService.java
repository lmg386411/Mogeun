package com.mogun.backend.service.report;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.report.muscleActInSet.MuscleActInSetLog;
import com.mogun.backend.domain.report.muscleActInSet.repository.MuscleActInSetLogRepository;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.routineReport.repository.RoutineReportRepository;
import com.mogun.backend.domain.report.routineResult.RoutineResult;
import com.mogun.backend.domain.report.routineResult.repository.RoutineResultRepository;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.domain.report.setReport.repository.SetReportRepository;
import com.mogun.backend.domain.user.User;
import com.mogun.backend.domain.user.repository.UserRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.attachPart.AttachPartService;
import com.mogun.backend.service.report.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
@Transactional
@RequiredArgsConstructor
public class RoutineResultService {

    private final UserRepository userRepository;
    private final RoutineReportRepository reportRepository;
    private final RoutineResultRepository resultRepository;
    private final SetReportRepository setReportRepository;
    private final AttachPartService attachPartService;
    private final MuscleActInSetLogRepository actInSetLogRepository;

    public ServiceStatus<Object> createResult(ResultDto dto) {

        Optional<RoutineReport> report = reportRepository.findById(dto.getReportKey());
        if(report.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록된 적 없는 루틴 기록");

        Optional<RoutineResult> result = resultRepository.findByRoutineReport(report.get());
        if(result.isPresent())
            return ServiceStatus.errorStatus("요청 오류: 기록된 루틴 결과에 대한 재작성");

        resultRepository.save(dto.toRoutineResultEntity(report.get()));

        return ServiceStatus.okStatus();
    }

    public ServiceStatus<Object> getAllInfoOfResult(ResultDto dto) {

        Optional<RoutineReport> report = reportRepository.findById(dto.getReportKey());
        if(report.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 루틴 기록이 없음");

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");

        if(!report.get().getUser().equals(user.get()))
            return ServiceStatus.errorStatus("요청 오류: 루틴 기록을 소유한 회원과 요청 회원이 불일치");

//        RoutineReport report = result.get().getRoutineReport();
        List<SetReport> setReportList = setReportRepository.findAllByRoutineReport(report.get());
        List<ExerciseResultDto> exerciseResultDtoList = new ArrayList<>();

//        // Seongmin 루틴에서 설정한 운동에 맞게 정렬
//        List<UserRoutinePlan> RoutineList = userRoutinePlanRepository.findAllByUserRoutine(report.getUserRoutine());
//        List<String> exerciseNameList = new ArrayList<>();
//
//        for (UserRoutinePlan routinePlan: RoutineList) {
//            exerciseNameList.add(routinePlan.getExercise().getName());
//
//            exerciseResultDtoList.add(ExerciseResultDto.builder()
//                    .execName(routinePlan.getExercise().getName())
//                    .imagePath(routinePlan.getExercise().getImagePath())
//                    .sets(0)
//                    .partList(new ArrayList<>())
//                    .setResultList(new ArrayList<>())
//                    .build());
//
//            // Seongmin muscle part -> part image path
//            exerciseResultDtoList.get(exerciseNameList.size() - 1).getPartList().addAll(attachPartService.getPartImagePathByExercise(routinePlan.getExercise()));
//        }
//
//        // Seongmin 같은 운동이지만 set가 1로 여러개 나오는 현상 수정 & 루틴에서 설정한 운동에 맞게 정렬
//        for (SetReport setReport: setReportList) {
//            int index = exerciseNameList.indexOf(setReport.getExercise().getName());
//            int lastSet = exerciseResultDtoList.get(index).getSets();
//            exerciseResultDtoList.get(index).setSets(lastSet + 1);
//            List<MuscleActInSetLog> logList = actInSetLogRepository.findAllBySetReport(setReport);
//            List<Float> activity = new ArrayList<>();
//            for (MuscleActInSetLog log : logList)
//                activity.add(log.getMuscleActivity());
//            exerciseResultDtoList.get(index).getSetResultList().add(SetResultDto.builder()
//                    .setNumber(setReport.getSetNumber())
//                    .weight(setReport.getTrainWeight())
//                    .targetRep(setReport.getTargetRep())
//                    .successRep(setReport.getSuccessesRep())
//                    .muscleActivityList(activity)
//                    .build());
//        }


        // 시간 순서로 정렬한 세트 기록에 대해
        for (SetReport setReport: setReportList) {
            // 운동 목록 자체가 비어 있으면 초기 작업
            if(exerciseResultDtoList.isEmpty()) {
                exerciseResultDtoList.add(ExerciseResultDto.builder()
                        .execName(setReport.getExercise().getName())
                        .imagePath(setReport.getExercise().getImagePath())
                        .sets(0)
                        .partList(new ArrayList<>())
                        .muscleImagePathList(new ArrayList<>())
                        .setResultList(new ArrayList<>())
                        .build());

                exerciseResultDtoList.get(0).getPartList().addAll(attachPartService.getMainSubPartNameByExercise(setReport.getExercise()));
                exerciseResultDtoList.get(0).getMuscleImagePathList().addAll(attachPartService.getPartImagePathByExercise(setReport.getExercise()));
            }

            // 목록의 끝부분을 가리키는 index 갱신 및 현재 세트가 무엇인지 확인
            int flag = 0;
            int lastIndex = exerciseResultDtoList.size() - 1;
            Exercise exec = setReport.getExercise();

            // 현재 setReport의 운동이 exDtoList에 포함되어 있는지 검사
            for(ExerciseResultDto resultDto: exerciseResultDtoList) {
                List<Double> actList = new ArrayList<>();
                List<Double> fatgList = new ArrayList<>();
                if(exec.getName().equals(resultDto.getExecName())) {

                    List<MuscleActInSetLog> setLogList = actInSetLogRepository.findAllBySetReport(setReport);
                    for(MuscleActInSetLog log: setLogList) {
                        actList.add(log.getMuscleActivity());
                        fatgList.add(log.getMuscleFatigue());
                    }

                    resultDto.setSets(resultDto.getSets() + 1);
                    resultDto.getSetResultList().add(SetResultDto.builder()
                            .setNumber(setReport.getSetNumber())
                            .targetRep(setReport.getTargetRep())
                            .successRep(setReport.getSuccessesRep())
                            .weight(setReport.getTrainWeight())
                            .muscleActivityList(actList)
                            .muscleFatigueList(fatgList)
                            .build());
                    flag = 1;
                    break;
                }
            }

            // 포함이 안 되어 있다면
            if(flag == 0) {
                // 초기 작업과 똑같이 새로운 exec 추가
                exerciseResultDtoList.add(ExerciseResultDto.builder()
                        .execName(setReport.getExercise().getName())
                        .imagePath(setReport.getExercise().getImagePath())
                        .sets(1)
                        .partList(new ArrayList<>())
                        .muscleImagePathList(new ArrayList<>())
                        .setResultList(new ArrayList<>())
                        .build());

                lastIndex = exerciseResultDtoList.size() - 1;
                exerciseResultDtoList.get(lastIndex).getPartList().addAll(attachPartService.getMainSubPartNameByExercise(setReport.getExercise()));
                exerciseResultDtoList.get(lastIndex).getMuscleImagePathList().addAll(attachPartService.getPartImagePathByExercise(setReport.getExercise()));

                List<Double> actList = actInSetLogRepository.findAllActivityBySetReport(setReport);
                List<Double> fatgList = actInSetLogRepository.findAllFatigueBySetReport(setReport);

                exerciseResultDtoList.get(lastIndex).getSetResultList().add(SetResultDto.builder()
                        .setNumber(setReport.getSetNumber())
                        .targetRep(setReport.getTargetRep())
                        .successRep(setReport.getSuccessesRep())
                        .weight(setReport.getTrainWeight())
                        .muscleActivityList(actList)
                        .muscleFatigueList(fatgList)
                        .build());
            }
        }

        Duration performTime = Duration.between(report.get().getStartTime(), report.get().getEndTime());
        Optional<RoutineResult> result = resultRepository.findByRoutineReport(report.get());

        SummaryResultDto data = SummaryResultDto.builder()
                .routineName(report.get().getRoutineName())
                .routineDate(result.get().getRoutineDate())
                .consumeCalorie(result.get().getConsumeCalorie())
                .totalSets(setReportList.size())
                .performTime(performTime.toMinutes())
                .exerciseResultDtoList(exerciseResultDtoList)
                .build();

        return ServiceStatus.builder()
                .status(100)
                .data(data)
                .build();
    }

    public ServiceStatus<List<SetResultListDto>> getExerciseResult(ResultDto dto) {

        List<SetResultListDto> list = new ArrayList<>();
        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty()) {
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");
        }

        Optional<RoutineResult> result = resultRepository.findById(dto.getResultKey());
        if(result.isEmpty()) {
            return ServiceStatus.errorStatus("요청 오류: 해당 루틴 기록이 없음");
        }

        if(!user.get().equals(result.get().getUser())) {
            return ServiceStatus.errorStatus("요청 오류: 루틴 기록을 소유한 회원과 요청 회원이 불일치");
        }

        List<SetReport> setReportList = setReportRepository.findAllByRoutineReport(result.get().getRoutineReport());

        for(SetReport report: setReportList) {
            if(list.isEmpty()) {
                list.add(SetResultListDto.builder()
                        .exec(report.getExercise())
                        .status(0)
                        .setResultDtoList(new ArrayList<>())
                        .build());
            }

            int lastIndex = list.size() - 1;
            Exercise exec = report.getExercise();

            if(!exec.equals(list.get(lastIndex).getExec())) {
                list.add(SetResultListDto.builder()
                        .exec(report.getExercise())
                        .setResultDtoList(new ArrayList<>())
                        .build());
            } else {
                List<MuscleActInSetLog> logList = actInSetLogRepository.findAllBySetReport(report);
                List<Double> activity = new ArrayList<>();
                List<Double> fatigue = new ArrayList<>();
                for (MuscleActInSetLog log: logList)
                    activity.add(log.getMuscleActivity());

                list.get(lastIndex).getSetResultDtoList().add(
                        SetResultDto.builder()
                                .setNumber(report.getSetNumber())
                                .weight(report.getTrainWeight())
                                .targetRep(report.getTargetRep())
                                .successRep(report.getSuccessesRep())
                                .muscleActivityList(activity)
                                .muscleFatigueList(fatigue)
                                .build()
                );
            }
        }

        return ServiceStatus.<List<SetResultListDto>>builder()
                .status(100)
                .message("SUCCESS")
                .data(list)
                .build();
    }

    public ServiceStatus<List<ResultListDto>> getLastMonthResult(ResultDto dto) {

        List<ResultListDto> list = new ArrayList<>();
        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty()) {
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");
        }

        LocalDate lastDate = LocalDate.now().minusMonths(1);
        List<RoutineResult> results = resultRepository.findAllByFromRoutineDateAndUser(lastDate, user.get());

        for(RoutineResult result: results) {
            if(list.isEmpty()) {
                list.add(ResultListDto.builder()
                        .date(result.getRoutineDate())
                        .routineCount(0)
                        .routineReports(new ArrayList<>())
                        .build());
            }

            int lastIndex = list.size() - 1;
            RoutineReport report = result.getRoutineReport();

            if(list.get(lastIndex).getDate().isEqual(result.getRoutineDate())) {
                list.get(lastIndex).getRoutineReports().add(SimpleReportInfo.builder()
                        .routineName(report.getRoutineName())
                        .reportKey(report.getRoutineReportKey())
                        .startTime(report.getStartTime())
                        .endTime(report.getEndTime())
                        .build());

                list.get(lastIndex).setRoutineCount(list.get(lastIndex).getRoutineCount() + 1);
            } else {
                list.add(ResultListDto.builder()
                        .date(result.getRoutineDate())
                        .routineCount(1)
                        .routineReports(new ArrayList<>())
                        .build());
                lastIndex = list.size() - 1;
                list.get(lastIndex).getRoutineReports().add(SimpleReportInfo.builder()
                        .routineName(report.getRoutineName())
                        .reportKey(report.getRoutineReportKey())
                        .startTime(report.getStartTime())
                        .endTime(report.getEndTime())
                        .build());
            }
        }
        return ServiceStatus.<List<ResultListDto>>builder()
                .status(100)
                .message("SUCCESS")
                .data(list)
                .build();
    }

    public ServiceStatus<List<ResultListDto>> getMonthlyRangeResult(ResultDto dto) {

        List<ResultListDto> list = new ArrayList<>();

        Optional<User> user = userRepository.findById(dto.getUserKey());
        if(user.isEmpty()) {
            return ServiceStatus.errorStatus("요청 오류: 등록된 회원이 아님");
        }

        LocalDate startDate = dto.getDate().with(firstDayOfMonth());
        LocalDate endDate = dto.getDate().with(lastDayOfMonth());

        List<RoutineResult> results = resultRepository.findAllByBetweenRoutineDateAndUser(startDate, endDate, user.get());

        for(RoutineResult result: results) {
            if(list.isEmpty()) {
                list.add(ResultListDto.builder()
                        .date(result.getRoutineDate())
                        .routineCount(0)
                        .routineReports(new ArrayList<>())
                        .build());
            }

            int lastIndex = list.size() - 1;
            RoutineReport report = result.getRoutineReport();

            if(list.get(lastIndex).getDate().isEqual(result.getRoutineDate())) {
                list.get(lastIndex).getRoutineReports().add(SimpleReportInfo.builder()
                        .routineName(report.getRoutineName())
                        .reportKey(report.getRoutineReportKey())
                        .startTime(report.getStartTime())
                        .endTime(report.getEndTime())
                        .build());

                list.get(lastIndex).setRoutineCount(list.get(lastIndex).getRoutineCount() + 1);
            } else {
                list.add(ResultListDto.builder()
                        .date(result.getRoutineDate())
                        .routineCount(1)
                        .routineReports(new ArrayList<>())
                        .build());
                lastIndex = list.size() - 1;
                list.get(lastIndex).getRoutineReports().add(SimpleReportInfo.builder()
                        .routineName(report.getRoutineName())
                        .reportKey(report.getRoutineReportKey())
                        .startTime(report.getStartTime())
                        .endTime(report.getEndTime())
                        .build());
            }
        }
        return ServiceStatus.<List<ResultListDto>>builder()
                .status(100)
                .message("SUCCESS")
                .data(list)
                .build();
    }
}
