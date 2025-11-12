package ru.inno.attestation.attestation03.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import ru.inno.attestation.attestation03.utils.interfaces.SortableField;


@Getter
@Setter
public  class ListRequestDto<F, S extends SortableField> {

    private F filter;
    private Integer page = 0;
    private Integer pageSize = 20;
    private Sort.Direction sortType = Sort.Direction.ASC;
    private S sortField;
}