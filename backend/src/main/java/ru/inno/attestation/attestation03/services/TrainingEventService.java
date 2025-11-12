package ru.inno.attestation.attestation03.services;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.inno.attestation.attestation03.dto.ListRequestDto;
import ru.inno.attestation.attestation03.dto.ListResponseDto;
import ru.inno.attestation.attestation03.dto.TrainingEventFilterDto;
import ru.inno.attestation.attestation03.dto.TrainingEventResponseDto;
import ru.inno.attestation.attestation03.mappers.TrainingEventMapper;
import ru.inno.attestation.attestation03.models.TrainingEvent;
import ru.inno.attestation.attestation03.repositories.TrainingEventRepository;
import ru.inno.attestation.attestation03.specifications.TrainingEventSpecification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrainingEventService {

    private final TrainingEventRepository repository;
    private final TrainingEventMapper mapper;


    public ListResponseDto<TrainingEventResponseDto> list(@Nullable ListRequestDto<TrainingEventFilterDto> request) {
        if (request == null) {
            request = new ListRequestDto<>();
        }
        Specification<TrainingEvent> specification = TrainingEventSpecification.getSpecification(request.getFilter());
        List<Sort.Order> orders = new ArrayList<>();

        if(request.getSortField() != null) {
            orders.add(new Sort.Order(request.getSortType() != null ? request.getSortType() : Sort.Direction.ASC, request.getSortField()));
        }
        PageRequest pageRequest = PageRequest.of(
                request.getPage(),
                request.getPageSize(),
                Sort.by(orders)
        );

        List<TrainingEventResponseDto> list = repository.findAll(specification, pageRequest).stream().map(mapper::toResponse).toList();
        Long totalCount = repository.count(specification);

        return ListResponseDto.<TrainingEventResponseDto>builder()
                .items(list)
                .totalCount(totalCount)
                .build();
    }
}
