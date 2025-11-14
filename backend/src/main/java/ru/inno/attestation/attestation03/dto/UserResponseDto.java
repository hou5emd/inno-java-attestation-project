package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.inno.attestation.attestation03.enums.UserRole;

public record UserResponseDto(Long id, String firstName, String lastName, @NotNull UserRole role,
                              @NotNull @Size String userName) {

}
