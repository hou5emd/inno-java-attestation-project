package ru.inno.attestation.attestation03.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public  class ListRequestDto<F> {
    private F filter;
    private Integer page;
    private Integer pageSize;
    private Sort.Direction sortType;
    // TODO: сделать генерацию через annotation-processor
    private String sortField;
}