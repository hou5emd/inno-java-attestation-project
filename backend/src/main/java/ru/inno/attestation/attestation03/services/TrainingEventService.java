package ru.inno.attestation.attestation03.services;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.exceptions.TrainingEventNotFoundException;
import ru.inno.attestation.attestation03.mappers.ListRequestDtoMapper;
import ru.inno.attestation.attestation03.mappers.TrainingEventMapper;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.repositories.TrainingEventRepository;
import ru.inno.attestation.attestation03.repositories.UserRepository;
import ru.inno.attestation.attestation03.specifications.TrainingEventSpecification;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TrainingEventService {

    private final TrainingEventRepository repository;
    private final TrainingEventMapper mapper;
    private final UserRepository userRepository;


    public TrainingEventListResponseDto list(@Nullable BaseListRequestDto<TrainingEventFilterDto> request) {

        Specification<TrainingEvent> specification = TrainingEventSpecification.getSpecification(request != null ? request.getFilter() : null);

        PageRequest pageRequest = ListRequestDtoMapper.toDefaultPageAndSize(request);

        List<TrainingEventResponseDto> list = repository.findAll(specification, pageRequest).stream().map(mapper::toResponse).toList();
        Long totalCount = repository.count(specification);

        return TrainingEventListResponseDto.builder()
                .items(list)
                .totalCount(totalCount)
                .build();
    }

    public TrainingEventResponseDto create(TrainingEventCreateRequestDto request) {
        TrainingEvent event = mapper.createRequestToEntity(request);
        if (request.studentIds() != null && !request.studentIds().isEmpty()) {
            event.setStudents(userRepository.findAllById(request.studentIds()));
        }
        return mapper.toResponse(repository.save(event));
    }

    public TrainingEventResponseDto getById(Long id) {
        TrainingEvent event = repository.findById(id).orElseThrow(() -> new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id)));
        if (event.getDeleted()) {
            throw new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id));
        }
        return mapper.toResponse(event);
    }

    @Transactional
    public void delete(Long id) {
        TrainingEvent event = repository.findById(id).orElseThrow(() -> new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id)));
        if (event.getDeleted()) {
            throw new TrainingEventNotFoundException(String.format("Тренировка с id = %d, уже удалена", id));
        }
        event.setDeleted(true);
    }

    public TrainingEventResponseDto update(Long id, TrainingEventUpdateRequestDto request) {
        TrainingEvent event = repository.findById(id).orElseThrow(() -> new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id)));
        if (event.getDeleted()) {
            throw new TrainingEventNotFoundException(String.format("Тренировка с id = %d, не найдена", id));
        }
        TrainingEvent updatedEvent = mapper.mergeWithUpdateRequestDto(event, request);
        if (request.studentIds() != null) {
            updatedEvent.setStudents(userRepository.findAllById(request.studentIds()));
        }
        return mapper.toResponse(repository.save(updatedEvent));
    }


}
