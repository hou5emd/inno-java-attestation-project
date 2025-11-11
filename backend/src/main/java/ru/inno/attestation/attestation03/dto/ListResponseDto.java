package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListResponseDto<E> {
    @NotNull
    private List<E> items;
    @NotNull
    private Long totalCount;
}
