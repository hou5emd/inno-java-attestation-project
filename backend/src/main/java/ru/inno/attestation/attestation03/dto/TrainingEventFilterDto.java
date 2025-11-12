package ru.inno.attestation.attestation03.dto;

import ru.inno.attestation.attestation03.models.TrainingEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link TrainingEvent}
 */
public record TrainingEventFilterDto(Long id, String description, LocalDateTime fromDate, LocalDateTime toDate,
                                     Long ownerId, List<Long> studentIds) {
}