package ru.inno.attestation.attestation03.services;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.inno.attestation.attestation03.dto.*;
import ru.inno.attestation.attestation03.mappers.ListRequestDtoMapper;
import ru.inno.attestation.attestation03.mappers.TrainingEventMapper;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.repositories.TrainingEventRepository;
import ru.inno.attestation.attestation03.specifications.TrainingEventSpecification;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TrainingEventService {

    private final TrainingEventRepository repository;
    private final TrainingEventMapper mapper;


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
}
