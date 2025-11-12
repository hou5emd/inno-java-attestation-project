package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link ru.inno.attestation.attestation03.models.TrainingEvent}
 */
@Value
public class TrainingEventResponseDto {
    @NotNull
    Long id;
    @NotNull
    String description;
    @NotNull
    LocalDateTime startDate;
    @NotNull
    LocalDateTime endDate;
    @NotNull
    UserResponseDto owner;
    List<UserResponseDto> students;
}