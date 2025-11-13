package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import ru.inno.attestation.attestation03.models.TrainingEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link TrainingEvent}
 */
@Value
public class TrainingEventCreateRequestDto {
    @NotNull
    String description;
    @NotNull
    LocalDateTime startDate;
    @NotNull
    LocalDateTime endDate;
    Long ownerId;
    List<Long> studentIds;
}