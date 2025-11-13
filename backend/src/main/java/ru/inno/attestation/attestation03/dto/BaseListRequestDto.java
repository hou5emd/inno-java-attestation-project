package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public  class BaseListRequestDto<F> {
    private F filter;
    @Min(0)
    private Integer page;
    @Min(1)
    private Integer pageSize;
    private Sort.Direction sortType;
    // TODO: сделать генерацию через annotation-processor
    private String sortField;
}