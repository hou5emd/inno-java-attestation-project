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

    private final TrainingEventService service;


    @PostMapping("/list")
    @Operation(summary = "Список тренировок", description = "Получение списка Тренировок с сортировкой и фильрацией", operationId = "listEvents")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TrainingEventListResponseDto> listEvents(@RequestBody @Nullable TrainingEventListRequestDto request) {
        TrainingEventListResponseDto events = service.list(request);
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

    // TODO: для финальной аттестации добавить получение ownerId из токена, исправить dto TrainingEventCreateRequestDto
    @PostMapping
    @Operation(summary = "Создание тренировки", description = "Создание стренировки", operationId = "createEvent")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<TrainingEventResponseDto> createEvent(@RequestBody TrainingEventCreateRequestDto request) {
        TrainingEventResponseDto response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Удаление Тренировки", operationId = "deleteEvent")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Boolean> deleteEvent(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение Тренировки по id", operationId = "getById")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TrainingEventResponseDto> getById(@PathVariable Long id) {
        TrainingEventResponseDto response = service.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление тренировки по id", operationId = "update")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TrainingEventResponseDto> update(@PathVariable Long id, @RequestBody TrainingEventUpdateRequestDto request) {
        TrainingEventResponseDto response = service.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
