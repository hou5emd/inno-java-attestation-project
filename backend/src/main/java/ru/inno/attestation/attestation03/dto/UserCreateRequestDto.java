package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.inno.attestation.attestation03.enums.UserRole;

/**
 * DTO for {@link ru.inno.attestation.attestation03.models.User}
 */
public record UserCreateRequestDto(String firstName, String lastName, @NotNull UserRole role,
                                   @NotNull @Size String userName, @NotNull @Size String password) {
}