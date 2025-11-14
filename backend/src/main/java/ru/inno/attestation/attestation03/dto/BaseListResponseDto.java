package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class BaseListResponseDto<E> {
    @NotNull
    private List<E> items;
    @NotNull
    private Long totalCount;
}
