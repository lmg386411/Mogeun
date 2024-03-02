package com.mogun.backend.service.exercise;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import com.mogun.backend.domain.musclePart.MusclePart;
import com.mogun.backend.domain.musclePart.repository.MusclePartRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.exercise.dto.ExerciseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final MusclePartRepository musclePartRepository;

    public ServiceStatus<List<Exercise>> getAllExercise() {

        List<Exercise> exerciseList = exerciseRepository.findAll();

        return ServiceStatus.<List<Exercise>>builder()
                .status(100)
                .data(exerciseList)
                .build();
    }

    public ServiceStatus<Exercise> getExercise(int execKey) {

        Optional<Exercise> exec = exerciseRepository.findById(execKey);
        if(exec.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: 해당 운동 정보가 없음");

        return ServiceStatus.<Exercise>builder()
                .status(100)
                .data(exec.get())
                .build();
    }

    public ServiceStatus<Object> createExercise(ExerciseDto dto) {

        Optional<MusclePart> musclePart = musclePartRepository.findById(dto.getPartKey());
        if(musclePart.isEmpty())
            return ServiceStatus.errorStatus("요청 오류: DB에 등록되지 않은 근육 부위");

        dto.setPart(musclePart.get());
        exerciseRepository.save(dto.toExerciseEntity());

        return ServiceStatus.okStatus();
    }
}
