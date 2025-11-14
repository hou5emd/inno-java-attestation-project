package ru.inno.attestation.attestation03.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingEventMapper {
    TrainingEventResponseDto toResponse(TrainingEvent event);

    default List<Long> studentsToStudentIds(List<User> students) {
        return students.stream().map(User::getId).toList();
    }

    @Mapping(source = "ownerId", target = "owner.id")
    TrainingEvent createRequestToEntity(TrainingEventCreateRequestDto trainingEventCreateRequestDto);

    TrainingEvent mergeWithUpdateRequestDto(@MappingTarget TrainingEvent event, TrainingEventUpdateRequestDto newEvent);
}
