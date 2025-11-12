package ru.inno.attestation.attestation03.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public  class ListRequestDto<F> {
    private F filter;
    private Integer page = 0;
    private Integer pageSize = 20;
    private Sort.Direction sortType = Sort.Direction.ASC;
    // TODO: сделать генерацию через annotation-processor
    private String sortField;
}