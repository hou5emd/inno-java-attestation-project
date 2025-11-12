package ru.inno.attestation.attestation03.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inno.attestation.attestation03.dto.ListRequestDto;
import ru.inno.attestation.attestation03.dto.ListResponseDto;
import ru.inno.attestation.attestation03.dto.TrainingEventFilterDto;
import ru.inno.attestation.attestation03.dto.TrainingEventResponseDto;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.services.TrainingEventService;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class TrainingEventResource {

    private final TrainingEventService trainingEventService;



    @GetMapping
    @Operation(summary = "Список тренировок", description = "Получение списка Тренировок с сортировкой и фильрацией", operationId = "listEvents")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ListResponseDto<TrainingEventResponseDto>> listEvents(@Nullable ListRequestDto<TrainingEventFilterDto> request) {
        ListResponseDto<TrainingEventResponseDto> events = trainingEventService.list(request);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }


}
