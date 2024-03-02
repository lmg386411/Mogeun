package com.mogun.backend.controller.musclePart;

import com.mogun.backend.ApiResponse;
import com.mogun.backend.controller.musclePart.request.MusclePartRequest;
import com.mogun.backend.controller.musclePart.response.MusclePartResponse;
import com.mogun.backend.service.ServiceStatus;
import com.mogun.backend.service.attachPart.MusclePartService;
import com.mogun.backend.service.attachPart.dto.MusclePartDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/API/Muscle")
public class MusclePartController {

    private final MusclePartService musclePartService;

    @ApiOperation(value = "근육 부위 등록 API", notes = "(개발용) 근육 부위를 등록한다.")
    @PostMapping("/Insert")
    public ApiResponse<Object> insertMusclePart(@RequestBody MusclePartRequest request) {

        ServiceStatus<Object> result = musclePartService.insertMusclePart(MusclePartDto.builder()
                .partName(request.getPartName())
                .imagePath(request.getImagePath())
                .build());

        return ApiResponse.postAndPutResponse(result, request);
    }

    @ApiOperation(value = "전체 근육 부위 조회 API", notes = "모든 근육 부위를 조회한다.")
    @GetMapping("/ListAll")
    public ApiResponse<Object> listAllMusclePart() {

        List<MusclePartResponse> responseList = new ArrayList<>();
        List<MusclePartDto> dtoList = musclePartService.listAllMusclePart();
        for(MusclePartDto dto: dtoList)
            responseList.add(MusclePartResponse.builder()
                    .partKey(dto.getPartKey())
                    .partName(dto.getPartName())
                    .imagePath(dto.getImagePath())
                    .build());

        return ApiResponse.ok(responseList);
    }
}
