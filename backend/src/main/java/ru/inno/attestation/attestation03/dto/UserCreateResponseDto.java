package ru.inno.attestation.attestation03.dto;

import jakarta.validation.constraints.NotNull;
import ru.inno.attestation.attestation03.enums.UserRole;
import ru.inno.attestation.attestation03.models.User;

/**
 * DTO for {@link User}
 */
public record UserCreateResponseDto(@NotNull Long id, String firstName, String lastName, @NotNull UserRole role,
                                    @NotNull String userName) {
}