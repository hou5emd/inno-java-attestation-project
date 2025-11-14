package ru.inno.attestation.attestation03.dto;

import ru.inno.attestation.attestation03.models.User;

/**
 * DTO for {@link User}
 */
public record UserUpdateRequestDto(String firstName, String lastName, String userName, String password) {
}