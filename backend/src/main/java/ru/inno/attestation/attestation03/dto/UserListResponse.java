package ru.inno.attestation.attestation03.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class UserListResponse extends ListResponseDto<UserResponseDto> {
}
