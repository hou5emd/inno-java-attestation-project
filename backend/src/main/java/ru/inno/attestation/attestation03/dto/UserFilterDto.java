package ru.inno.attestation.attestation03.dto;

import ru.inno.attestation.attestation03.enums.UserRole;
import ru.inno.attestation.attestation03.models.User;

/**
 * DTO for {@link User}
 */
public record UserFilterDto(Long id, String firstName, String lastName, UserRole role, String userName) {
}