package ru.inno.attestation.attestation03.mappers;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.inno.attestation.attestation03.dto.BaseListRequestDto;

import java.util.ArrayList;
import java.util.List;

public class ListRequestDtoMapper {
    public static PageRequest toDefaultPageAndSize(BaseListRequestDto<?> request) {
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println(request);
        List<Sort.Order> orders = new ArrayList<>();
        int page = request != null && request.getPage() != null ? request.getPage() : 0;
        int pageSize = request != null && request.getPageSize() != null ? request.getPageSize() : 25;

        if(request != null && request.getSortField() != null) {
            orders.add(new Sort.Order(request.getSortType() != null ? request.getSortType() : Sort.Direction.ASC, request.getSortField()));
        }

        return PageRequest.of(page, pageSize, Sort.by(orders));
    }
}
