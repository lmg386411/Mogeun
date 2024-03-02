package com.mogun.backend.service.attachPart;

import com.mogun.backend.domain.musclePart.MusclePart;
import com.mogun.backend.domain.musclePart.repository.MusclePartRepository;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.attachPart.dto.MusclePartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MusclePartService {

    private final MusclePartRepository musclePartRepository;

    public ServiceStatus<Object> insertMusclePart(MusclePartDto dto) {

        musclePartRepository.save(dto.toMusclePartEntity());

        return ServiceStatus.okStatus();
    }

    public List<MusclePartDto> listAllMusclePart() {

        List<MusclePartDto> result = new ArrayList<>();
        List<MusclePart> partList = musclePartRepository.findAll();
        for(MusclePart part: partList) {

            result.add(MusclePartDto.builder()
                    .partKey(part.getPartKey())
                    .partName(part.getPartName())
                    .imagePath(part.getImagePath())
                    .build());
        }

        return result;
    }
}
