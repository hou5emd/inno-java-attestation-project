package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import ru.inno.attestation.attestation03.models.TrainingEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link TrainingEvent}
 */
public record TrainingEventUpdateRequestDto(@NotNull String description, @NotNull LocalDateTime startDate,
                                            @NotNull LocalDateTime endDate, List<Long> studentIds) {
}