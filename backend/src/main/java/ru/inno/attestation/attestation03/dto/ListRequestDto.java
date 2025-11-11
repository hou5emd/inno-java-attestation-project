package ru.inno.attestation.attestation03.dto;

import lombok.Getter;
import org.springframework.data.domain.Sort;
import ru.inno.attestation.attestation03.enums.SortType;

import java.util.Map;

@Getter
public class ListRequestDto<F> {

    private F filter;
    private Integer page;
    private Integer pageSize;
    private Sort.Direction sortType;
    private String sortField;
}