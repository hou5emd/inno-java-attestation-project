package ru.inno.attestation.attestation03.dto;

import ru.inno.attestation.attestation03.enums.UserRole;
import ru.inno.attestation.attestation03.models.User;
import ru.inno.attestation.attestation03.utils.annotations.GenerateSortFields;

/**
 * DTO for {@link User}
 */
@GenerateSortFields
public record UserFilterDto(Long id, String firstName, String lastName, UserRole role, String userName) {
}