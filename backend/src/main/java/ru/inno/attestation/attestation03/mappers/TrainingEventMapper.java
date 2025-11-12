package ru.inno.attestation.attestation03.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.inno.attestation.attestation03.dto.TrainingEventFilterDto;
import ru.inno.attestation.attestation03.dto.TrainingEventResponseDto;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainingEventMapper {
    TrainingEventResponseDto toResponse(TrainingEvent event);

    @Mapping(source = "ownerId", target = "owner.id")
    TrainingEvent filterToEntity(TrainingEventFilterDto trainingEventFilterDto);

    @Mapping(target = "studentIds", expression = "java(studentsToStudentIds(trainingEvent.getStudents()))")
    @Mapping(source = "owner.id", target = "ownerId")
    TrainingEventFilterDto toTrainingEventFilterDto(TrainingEvent trainingEvent);

    default List<Long> studentsToStudentIds(List<User> students) {
        return students.stream().map(User::getId).toList();
    }
}
