package ru.inno.attestation.attestation03.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.services.TrainingEventService;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class TrainingEventResource {

    private final TrainingEventService trainingEventService;



    @GetMapping
    @Operation(summary = "Список тренировок", description = "Получение списка Тренировок с сортировкой и фильрацией", operationId = "listEvents")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TrainingEventListResponseDto> listEvents(@ModelAttribute @Nullable ListRequestDto<TrainingEventFilterDto> request) {
        TrainingEventListResponseDto events = trainingEventService.list(request);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }


}
