package com.mogun.backend.service.attachPart;

import com.mogun.backend.domain.attachPart.AttachPart;
import com.mogun.backend.domain.attachPart.repository.AttachPartRepository;
import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachPartService {

    private final ExerciseRepository exerciseRepository;
    private final AttachPartRepository attachPartRepository;

    public List<String> getAllPartNameByExercise(Exercise exec) {

        List<String> result = new ArrayList<>();
        List<AttachPart> partList = attachPartRepository.findAllByExercise(exec);

        for(AttachPart item: partList) {

            if(item.getAttachDirection() == 'L')
                result.add("왼쪽 " + item.getMusclePart().getPartName());
            else
                result.add("오른쪽 " + item.getMusclePart().getPartName());
        }

        return result;
    }

    // Seongmin 사용 근육 왼쪽 오른쪽 구분 X
    public List<String> getPartNameByExercise(Exercise exec) {

        List<String> result = new ArrayList<>();
        List<AttachPart> partList = attachPartRepository.findAllByExerciseAndAttachDirection(exec, 'L');

        for(AttachPart item: partList) {
            result.add(item.getMusclePart().getPartName());
        }

        return result;
    }

    public List<String> getMainSubPartNameByExercise(Exercise exec) {

        List<String> result = new ArrayList<>();
        List<AttachPart> partList = attachPartRepository.findAllByExerciseAndAttachDirection(exec, 'L');

        for(AttachPart item: partList) {
            if(item.getMuscleCategory() == 'M')
                result.add("주 " + item.getMusclePart().getPartName());
            else
                result.add("부 " + item.getMusclePart().getPartName());
        }

        return result;
    }

    // Seongmin 이미지 가져오기
    public List<String> getPartImagePathByExercise(Exercise exec) {

        List<String> result = new ArrayList<>();
        List<AttachPart> partList = attachPartRepository.findAllByExerciseAndAttachDirection(exec, 'L');

        for(AttachPart item: partList) {
            result.add(item.getMusclePart().getImagePath());
        }

        return result;
    }
}
