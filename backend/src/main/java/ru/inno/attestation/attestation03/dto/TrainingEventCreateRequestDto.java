package ru.inno.attestation.attestation03.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import jakarta.validation.constraints.NotNull;
import ru.inno.attestation.attestation03.models.TrainingEvent;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link TrainingEvent}
 */
public record TrainingEventCreateRequestDto(@NotNull String description, @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime startDate,
                                            @NotNull @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @JsonDeserialize(using = LocalDateTimeDeserializer.class) @JsonSerialize(using = LocalDateTimeSerializer.class) LocalDateTime endDate, Long ownerId, List<Long> studentIds) {
}